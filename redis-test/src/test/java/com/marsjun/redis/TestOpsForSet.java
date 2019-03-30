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
import java.util.Set;

/**
 * @Author marsJun
 * @Date 2019/3/28.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestOpsForSet {

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * set
     *
     * @see BoundSetOperations 先绑定key,操作与SetOperations类似
     */
    @Test
    public void testOpsForValue() {
//        testPutAndGet();
//        testDifference();
        testInterest();
//        testUnion();
//        testMember();
    }


    private void testPutAndGet() {
        SetOperations opsForSet = redisTemplate.opsForSet();
        Long num = opsForSet.add("aa", 3, "c", 5, 6);
        System.err.println("add num : " + num);

        Object aa = opsForSet.pop("aa");
        System.err.println("pop key aa : " + aa);

        Long size = opsForSet.size("aa");
        for (int i = 0; i < size; i++) {
            opsForSet.pop("aa");
        }
        //add num : 4
        //pop key aa : 5
    }

    private void testDifference() {
        SetOperations opsForSet = redisTemplate.opsForSet();
        /**
         * 求差集,即在"aa"中存在,在"bb"中不存在的数据
         */
        opsForSet.add("aa", "1", "2", "3");
        opsForSet.add("bb", "1", "2", "4");
        Set difference = opsForSet.difference("aa", "bb");
        System.err.printf("difference set :%s\n", difference);
        //difference set :[3]
    }

    private void testInterest() {
        SetOperations opsForSet = redisTemplate.opsForSet();
        opsForSet.add("aa", "1", "2", "3");
        opsForSet.add("bb", "1", "2", "4");
        /**
         * 字面意思,两个都感兴趣的,即交集,可以用来做共同好友的功能
         */
        Set intersect = opsForSet.intersect("aa", "bb");
        System.err.printf("intersect set :%s\n", intersect);

        /**
         * 求交集后存储到"cc"中
         */
        Long numStore = opsForSet.intersectAndStore("aa", "bb", "cc");
        System.err.println("numStore :" + numStore);
        Long cc = opsForSet.size("cc");
        for (int i = 0; i < cc; i++) {
            Object cc1 = opsForSet.pop("cc");
            System.err.println("cc: " + cc1);
        }
        //intersect set :[2, 1]
        //numStore :2
        //cc: 1
        //cc: 2
    }

    private void testUnion(){
        SetOperations opsForSet = redisTemplate.opsForSet();
        opsForSet.add("aa", "1", "2", "3");
        opsForSet.add("bb", "1", "2", "4");
        /**
         * 求并集
         */
        Set union = opsForSet.union("aa", "bb");
        System.err.println("union: " + union);
        //union: [1, 2, 4, 3]
    }

    private void testMember(){
        SetOperations opsForSet = redisTemplate.opsForSet();
        opsForSet.add("aa", "1", "2", "3");
        /**
         * 返回set中一个随机的元素,并不会改变原set集合
         * 指令:SRANDMEMBER
         * however while SPOP also removes the randomly selected element from the set,
         * SRANDMEMBER will just return a random element without altering the original set in any way.
         */
        Long prefix = opsForSet.size("aa");
        Object ff = opsForSet.randomMember("aa");
        Long after = opsForSet.size("aa");
        System.err.printf("prefix :%d,after: %d\n", prefix, after);
        System.err.println(ff);
        /**
         * 判断元素"1"是否是set "aa"中的元素
         * 存在返回true,不存在返回false
         */
        opsForSet.isMember("aa", "1");

        /**
         * 得到"aa"集合中所有的元素
         */
        Set allSet = opsForSet.members("aa");
        System.err.printf("set: %s\n", allSet);
        /**
         * 从键为key的集合中随机获取count个元素
         */
        Set distinctRandomMembers = opsForSet.distinctRandomMembers("aa", 2);
        System.err.println("distinctRandomMembers :" + distinctRandomMembers);
        //prefix :3,after: 3
        //1
        //set: [2, 1, 3]
        //distinctRandomMembers :[1, 2]
    }

}
