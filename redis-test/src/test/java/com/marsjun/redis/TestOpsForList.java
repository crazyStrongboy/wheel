package com.marsjun.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.*;
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
public class TestOpsForList {

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 测试发现,当list队列中没有元素时,redis会将与list关联的key移除
     *
     * @see BoundListOperations 使用这个类即先绑定key,后续操作均和ListOperations类似
     */
    @Test
    public void testOpsForValue() {
//        testPushPop();
//        testPushAllAndRange();
//        testIndex();
        testRemove();
    }


    private void testPushPop() {
        ListOperations opsForList = redisTemplate.opsForList();
        opsForList.rightPush("aa", "aa"); // 往队列aa中存入 "aa"
        Object aa = opsForList.rightPop("aa");
        System.err.println("aa queue: " + aa);
        //aa queue: aa
    }

    private void testPushAllAndRange() {
        ListOperations opsForList = redisTemplate.opsForList();
        opsForList.rightPushAll("aa", "1", 2, "dd");
        //遍历
        List range = opsForList.range("aa", 0, 10);
        range.forEach(value -> System.err.println(value));
        //1
        //2
        //dd
    }

    private void testIndex() {
        ListOperations opsForList = redisTemplate.opsForList();
        opsForList.rightPushAll("aa", "1", 2, "dd");
        Object aa1 = opsForList.index("aa", 1); //取出list中索引为index的值
        System.err.println(aa1);
        // 2
        Long size = opsForList.size("aa");
        System.err.println("size :" + size);
        // size :3
    }

    private void testRemove() {
        ListOperations opsForList = redisTemplate.opsForList();
        opsForList.rightPush("aa", "1");
        opsForList.rightPush("aa", "2");
        opsForList.rightPush("aa", "1");

        /**
         * count > 0: Remove elements equal to value moving from head to tail.
         * count < 0: Remove elements equal to value moving from tail to head.
         * count = 0: Remove all elements equal to value.
         */
        Long aa2 = opsForList.remove("aa", 1, "1");
        System.err.println(aa2); // 返回的是移除元素的个数
    }

}
