package com.marsjun.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.Set;

/**
 * @Author marsJun
 * @Date 2019/3/28.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestOpsForZSet {

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * zset
     * @see BoundZSetOperations 先绑定key,操作与ZSetOperations类似
     */
    @Test
    public void testOpsForValue() {
        testAddZSet();
        testRange();
    }


    private void testAddZSet() {
        ZSetOperations opsForZSet = redisTemplate.opsForZSet();
        Set<ZSetOperations.TypedTuple<String>> typedTuples = new HashSet<>();
        typedTuples.add(new DefaultTypedTuple("a", 0.8));
        typedTuples.add(new DefaultTypedTuple("b", 0.9));
        typedTuples.add(new DefaultTypedTuple("c", 0.7));
        opsForZSet.add("myset", typedTuples);
        opsForZSet.add("myset","d",0.5);

        /**
         * 获取总数量
         */
        Long size = opsForZSet.zCard("myset");
        System.err.println("size :" + size);
    }

    private void testRange(){
        ZSetOperations opsForZSet = redisTemplate.opsForZSet();
        Set myset = opsForZSet.rangeByScore("myset", 0.7, 0.8);
        System.err.println(myset);

        /**
         * 会按分数从低到高,索引0-based
         * Returns the rank of member in the sorted set stored at key, with the scores
         * ordered from low to high.The rank (or index) is 0-based, which means that
         * the member with the lowest score has rank 0.
         */
        Long rank = opsForZSet.rank("myset", "b");
        System.err.println("rank: "+rank);
        /**
         * 与上面的rank正好相反
         */
        Long reverseRank = opsForZSet.reverseRank("myset", "b");
        System.err.println("reverseRank: "+reverseRank);
        /**
         * count 是返回结果的个数
         * offset是从偏移量是多少的位置开始搜索
         */
        Set myset1 = opsForZSet.reverseRangeByScore("myset", 0.7, 0.8, 0, 1);
        System.err.println(myset1);
        //[c, a]
        //size :4
        //rank: 3
        //reverseRank: 0
        //[a]
    }

}
