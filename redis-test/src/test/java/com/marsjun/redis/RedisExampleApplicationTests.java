package com.marsjun.redis;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @Author marsJun
 * @Date 2019/3/28.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisExampleApplicationTests {

    @Autowired
    private RedisTemplate redisTemplate;

    @Before
    public void init() {
//        redisTemplate.setKeySerializer(new StringRedisSerializer());
//		redisTemplate.setValueSerializer(new StringRedisSerializer());
//		redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
    }

    private String cacheKey = "cacheKey"; //缓存key

    @Test
    public void contextLoads() {
    }


    /**
     * string
     * @see BoundValueOperations 使用这个类即先绑定key,后续操作均和ValueOperations类似
     */
    @Test
    public void testOpsForValue() {
        // 用来设置值是用String序列化,为了方便客户端观察,可以不用设置
        // 默认JdkSerializationRedisSerializer
//		redisTemplate.setValueSerializer(new StringRedisSerializer());
        ValueOperations opsForValue = redisTemplate.opsForValue();
        opsForValue.set("aa", "aa");
        opsForValue.set("bb", "bb", 10, TimeUnit.SECONDS); //设置过期时间
        Boolean aBoolean = opsForValue.setIfAbsent("aa", "cc");//不存在则设置
        System.err.println("exist :" + aBoolean);
        Boolean aBoolean1 = opsForValue.setIfAbsent("cc", "cc");
        System.err.println("not exist :" + aBoolean1);
        opsForValue.set("cc", "dd", 1); //在偏移量offset位置插入value
        opsForValue.increment("dd", 1); //用来进行自增

        opsForValue.append("aa", "ccc"); //键为"aa",例如"aa"+"ccc"===>"aaccc"
        opsForValue.setBit("ff", 1, true);
        Boolean ff1 = opsForValue.getBit("ff", 1);
        System.err.println("bit exist :" + ff1);
        Boolean ff2 = opsForValue.getBit("ff", 2); //这个应该可以用来做过滤器,感觉和BloomFilter类似
        System.err.println("bit not exist :" + ff2);


        Map<String, Integer> map = new HashMap<>();
        map.put("test1", 1);
        map.put("test2", 2);
        map.put("test3", 3);
        map.put("test4", 4);
        opsForValue.multiSet(map); //可以设置多个键值对
    }

    /**
     * 测试发现,当list队列中没有元素时,redis会将与list关联的key移除
     * @see BoundListOperations 使用这个类即先绑定key,后续操作均和ListOperations类似
     */
    @Test
    public void testOpsForList() {
        ListOperations opsForList = redisTemplate.opsForList();
        opsForList.rightPush("aa", "aa"); // 往队列aa中存入 "aa"
        Object aa = opsForList.rightPop("aa");
        System.err.println("aa queue: " + aa);

        opsForList.rightPushAll("aa", "1", 2, "dd");
        //遍历
        List range = opsForList.range("aa", 0, 10);
        range.forEach(value -> System.err.println(value));

        Object aa1 = opsForList.index("aa", 1); //取出list中索引为index的值
        System.err.println(aa1);

        Long size = opsForList.size("aa");
        System.err.println("size :" + size);
        for (int i = 0; i < size; i++) {
            opsForList.leftPop("aa");
        }
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

    /**
     * hash
     * @see BoundHashOperations 先绑定key,操作与HashOperations类似
     */
    @Test
    public void testOpsForHash() {
        HashOperations opsForHash = redisTemplate.opsForHash();
        opsForHash.put("aa", "1", "bb");
        opsForHash.put("aa", "2", "cc");
        Object aa = opsForHash.get("aa", "1");
        System.err.printf("result : %s \n", aa);

        Map map = opsForHash.entries("aa");
        map.forEach((key, value) -> System.err.printf("%s===%s\n", key, value));

        /**
         * 进行批量查询
         */
        List<String> collection = new ArrayList();
        collection.add("1");
        collection.add("2");
        List list = opsForHash.multiGet("aa", collection);
        System.err.println(list);

    }

    /**
     * set
     * @see BoundSetOperations 先绑定key,操作与SetOperations类似
     */
    @Test
    public void testOpsForSet() throws IOException {
        SetOperations opsForSet = redisTemplate.opsForSet();
        Long num = opsForSet.add("aa", 3, "c", 5, 6);
        System.err.println("add num : " + num);

        Object aa = opsForSet.pop("aa");
        System.err.println("pop key aa : " + aa);

        Long size = opsForSet.size("aa");
        for (int i = 0; i < size; i++) {
            opsForSet.pop("aa");
        }

        /**
         * 求差集,即在"aa"中存在,在"bb"中不存在的数据
         */
        opsForSet.add("aa", "1", "2", "3");
        opsForSet.add("bb", "1", "2", "4");
        Set difference = opsForSet.difference("aa", "bb");
        System.err.printf("difference set :%s\n", difference);

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
        /**
         * 求并集
         */
        Set union = opsForSet.union("aa", "bb");
        System.err.println("union: " + union);

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

        // 判断元素"1"是否是set "aa"中的元素
        opsForSet.isMember("aa", "1");

        /**
         * 得到"aa"集合中所有的元素
         */
        Set allSet = opsForSet.members("aa");
        System.err.printf("set: %s\n", allSet);
        /**
         * 从键为key的集合中随机获取count个元素
         */
        Set distinctRandomMembers = opsForSet.distinctRandomMembers("aa", 3);
        System.err.println("distinctRandomMembers :" + distinctRandomMembers);

        //TODO 有待测试
//        ScanOptions scanOptions = ScanOptions.scanOptions().match("c").count(10000).build();
//        Cursor cursor = opsForSet.scan("aa", scanOptions);
//        while (cursor.hasNext()) {
//            System.err.println("element: "+cursor.next());
//        }
//        cursor.close();

    }

    /**
     * zset
     * @see BoundZSetOperations 先绑定key,操作与ZSetOperations类似
     */
    @Test
    public void testOpsForZSet() {
        ZSetOperations opsForZSet = redisTemplate.opsForZSet();
        Set<ZSetOperations.TypedTuple<String>> typedTuples = new HashSet<>();
        typedTuples.add(new DefaultTypedTuple("a", 0.8));
        typedTuples.add(new DefaultTypedTuple("b", 0.9));
        typedTuples.add(new DefaultTypedTuple("c", 0.7));
        opsForZSet.add("myset", typedTuples);
        opsForZSet.add("myset","d",0.5);

        Set myset = opsForZSet.rangeByScore("myset", 0.7, 0.8);
        System.err.println(myset);

        /**
         * 获取尺寸
         */
        Long size = opsForZSet.zCard("myset");
        System.err.println("size :" + size);
        /**
         * 会按分数从低到高,索引0-based
         * Returns the rank of member in the sorted set stored at key, with the scores ordered from low to high.
         * The rank (or index) is 0-based, which means that the member with the lowest score has rank 0.
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
    }
}
