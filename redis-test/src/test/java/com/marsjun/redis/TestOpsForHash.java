package com.marsjun.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author marsJun
 * @Date 2019/3/28.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestOpsForHash {

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * hash
     * @see BoundHashOperations 先绑定key,操作与HashOperations类似
     */
    @Test
    public void testOpsForValue() {
//        testPutAndGet();
//        testEntities();
        testBatchSearch();
    }


    private void testPutAndGet() {
        HashOperations opsForHash = redisTemplate.opsForHash();
        opsForHash.put("aa", "1", "bb");
        opsForHash.put("aa", "2", "cc");
        Object aa = opsForHash.get("aa", "1");
        System.err.printf("result : %s \n", aa);
        //result : bb
    }

    private void testEntities(){
        HashOperations opsForHash = redisTemplate.opsForHash();
        opsForHash.put("aa", "1", "bb");
        opsForHash.put("aa", "2", "cc");
        Map map = opsForHash.entries("aa");
        map.forEach((key, value) -> System.err.printf("%s===%s\n", key, value));
        //2===cc
        //1===bb
    }

    private void testBatchSearch(){
        HashOperations opsForHash = redisTemplate.opsForHash();
        opsForHash.put("aa", "1", "bb");
        opsForHash.put("aa", "2", "cc");
        /**
         * 进行批量查询
         */
        List<String> collection = new ArrayList();
        collection.add("1");
        collection.add("2");
        List list = opsForHash.multiGet("aa", collection);
        System.err.println(list);
        //[bb, cc]
    }

}
