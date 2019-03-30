package com.marsjun.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author marsJun
 * @Date 2019/3/28.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestOpsForValue {

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * string
     *
     * @see BoundValueOperations 使用这个类即先绑定key,后续操作均和ValueOperations类似
     */
    @Test
    public void testOpsForValue() {
//        testSet();
//        testSetBit();
//        testSetMap();
        testAppend();
    }


    private void testSet() {
        ValueOperations opsForValue = redisTemplate.opsForValue();
        opsForValue.set("aa", "aa");
        /**
         * 设置过期时间,这里是10秒后过期
         */
        opsForValue.set("bb", "bb", 10, TimeUnit.SECONDS);
        /**
         * 不存在即设置,当键"aa"不存在时,设置其值为"cc",并返回true.
         * 如果存在,则返回false
         */
        Boolean exist = opsForValue.setIfAbsent("aa", "cc");
        System.err.println("exist: " + exist);
        Boolean notExist = opsForValue.setIfAbsent("cc", "cc");
        System.err.println("not exist: " + notExist);
        //exist: false
        //not exist: true
    }

    private void testSetBit() {
        /**
         * 这个应该可以用来做过滤器,感觉和BloomFilter类似
         */
        ValueOperations opsForValue = redisTemplate.opsForValue();
        opsForValue.setBit("ff", 1, true);
        Boolean ff1 = opsForValue.getBit("ff", 1);
        System.err.println("bit exist :" + ff1);
        Boolean ff2 = opsForValue.getBit("ff", 2);
        System.err.println("bit not exist :" + ff2);
        //bit exist :true
        //bit not exist :false
    }

    private void testSetMap() {
        /**
         * 可以设置多个键值对
         */
        ValueOperations opsForValue = redisTemplate.opsForValue();
        Map<String, Integer> map = new HashMap<>();
        map.put("test1", 1);
        map.put("test2", 2);
        map.put("test3", 3);
        map.put("test4", 4);
        opsForValue.multiSet(map);
        List<String> keys = new ArrayList<>();
        keys.add("test1");
        keys.add("test3");
        List list = opsForValue.multiGet(keys);
        System.err.println("multiGet: " + list);
        //multiGet: [1, 3]
    }

    private void testAppend() {
        /**
         * 这里必须要是string才能使用这两个方法,所以用StringRedisSerializer进行序列化和反序列化
         * set(key,value.offset)
         * append(key,value)
         *
         * @doc If key already exists and is a string, this command appends the value at the end of the string.
         */
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        ValueOperations opsForValue = redisTemplate.opsForValue();
        opsForValue.set("cc", "cc");
        opsForValue.set("cc", "dd", 1); //在偏移量offset位置插入value
        opsForValue.set("aa", "aa");
        opsForValue.append("aa", "ccc"); //键为"aa",例如"aa"+"ccc"===>"aaccc"
        System.err.println("aa: " + opsForValue.get("aa")+" ===== cc: " + opsForValue.get("cc"));
        //aa: aaccc ===== cc: cdd
    }
}
