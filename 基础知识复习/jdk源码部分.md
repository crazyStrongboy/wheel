#### ArrayList
1. new ArrayList();时，其中默认给数组赋值为{}，长度为0。在第一次添加元素时，扩容为默认的长度10。数组的扩容是一个非常耗费性能的操作，底层仅仅是将原数组的所有数据copy到一个新的数据中。在编写代码的过程中应该尽量避免数组的扩容。
2. 在序列化和反序列化的时候，只是对有效数据进行操作。





#### Vector
1. Vector也是实现的List接口，底层数据结构与ArrayList一致，但是所有的方法上都加上了`synchronized ` 关键字，使用起来开销就会特别大。所以说Vector是一个同步容器。使用其内提供的所有自带方法都是线程安全的。




#### LinkedList
1. LinkedList与ArrayList比较起来，增删改效率还是比较高的，但是查询效率比起来就略显不足了，咱们看下查询的代码段：

```
   
	Node<E> node(int index) {
        if (index < (size >> 1)) {
            Node<E> x = first;
            for (int i = 0; i < index; i++)
                x = x.next;
            return x;
        } else {
            Node<E> x = last;
            for (int i = size - 1; i > index; i--)
                x = x.prev;
            return x;
        }
    }
```

当索引在0-size/2之间时从0开始遍历，当索引在size/2-size之间时，则从end处开始遍历。当index靠近size/2时，效率可谓是十分低下。




----------



#### HashMap
1. HashMap是基于数组和链表实现的，在hash碰撞后就会在碰撞点形成链表结构。所以当hash碰撞过多时，链表会过于冗长，也不利于查询操作。
2. 遍历时尽量使用entryset方式去遍历，这样可以一次性得出所有的key和value。
3. 在并发情况下容易出现死循环，在resize过程中，容易出现环形链表。当一个key在map中不存在，而hash后刚好是环形链表的下标index时便会出现死循环。


```

	     for (Entry<K,V> e = table[indexFor(hash, table.length)];
             e != null;
             e = e.next) {
            Object k;
            if (e.hash == hash &&
                ((k = e.key) == key || (key != null && key.equals(k))))
                return e;
        }

```

如上面的代码段，在环形链表中，e.next始终不会为空，这样会导致外圈死循环一直运行，最终导致CPU飙升为100%。这个只会在jdk1.7中存在，jdk1.8不会出现死循环的问题。jdk1.8的resize设计与1.8之前的略有不同，并且提升了hashmap的性能，增加了随机性，resize后key要么在原先的index上，要么在index+old_length上，old_length为扩容前hashmap的长度。且在jdk1.8中不会出现环形链表的问题。

#### ConcurrentHashMap
1. 是一个支持并发的HashMap容器。
2. jdk1.7时，该容器内部分为多个Segment,只有操作同一个Segment的时候才需要通过锁（ReentrantLock）来进行同步，提高了并发效率。jdk1.8时，该容器的实现是利用CAS+synchronized（synchronized在jdk1.8中有很大的优化）来保证数据安全的共享的。

#### HashSet
HashSet主要利用HashMap来完成的，key为想存入的值，value默认给一个PRESENT(`private static final Object PRESENT = new Object();`)这个值。

#### LinkedHashMap
由于HashMap是一个无序的集合，遍历出来的顺序和写入的顺序是不能保持一致的，所以LinkedHashMap诞生了。主要关注下`newNode`这个方法。

```

   	Node<K,V> newNode(int hash, K key, V value, Node<K,V> e) {
        LinkedHashMap.Entry<K,V> p =
            new LinkedHashMap.Entry<K,V>(hash, key, value, e);
        linkNodeLast(p);
        return p;
    }

  	 private void linkNodeLast(LinkedHashMap.Entry<K,V> p) {
        LinkedHashMap.Entry<K,V> last = tail;
        tail = p;
        if (last == null)
            head = p;
        else {
            p.before = last;
            last.after = p;
        }
    }
```

在LinkedHashMap中重写了HashMap中的newChild这个方法，这样可以插入的每个值都会用before和after记录下前一个和后一个值。也就是双向链表。


----------


#### 多线程
1. 多线程的上下文切换是非常耗费性能的，在实际的生产过程中，应该尽量避免上下文切换。
> 1.在开发过程中，我们应该在保证数据安全共享的情况下尽量避免使用锁，可以将数据分段取模，每个线程仅仅只需要处理分配给自己的那一部分数据。
> 
> 2.采用CAS算法。
> 
> 3.合理的创建线程，避免创建的大部分线程处于waiting状态。

#### 线程池
##### 使用线程池的目的：

1. 线程是稀缺的资源，不能频繁的创建。
2. 解耦作用，线程的创建于执行任务单独的分开，方便维护。
3. 可以进行复用。

##### 执行流程：
![](https://i.imgur.com/ZO5A91u.png)

##### 如何配置线程池:

1. IO密集型任务：由于线程并不一定是一直在运行，则咱们可以多配置线程，2*CPU。
2. CPU密集型任务（大量复杂运算）：少配置线程数，CPU个数相当即可。

##### 线程池隔离
咱们需要根据自己的业务需求将线程池进行隔离。不能让一部分业务占用完整个线程池导致其他业务无法工作。


#### 多线程的三大核心
1. 原子性：一个操作要么全部执行成功，要么全部执行失败。

2. 可见性：多个线程能够同时观察到共享变量的变化。volatile可以保证可见性。[浅析volatile原理及其使用](https://www.hcyhj.cn/2018/11/17/volatile/index.html "浅析volatile原理及其使用")。

3. 顺序性：避免在多线程环境下指令重排后导致一些异常结果。


#### 线程之间进行通讯的几种方式
1. 等待通知机制：wait notify。
2. join()方法：在t2线程中嵌入t1.join()方法，则会阻塞直到t1线程执行完毕。
3. volatile共享。
4. CountDownLatch并发工具：可以保证多个任务都执行完成后再执行下一步操作。
![](https://i.imgur.com/1OfEzBZ.png)
5. CyclicBarrier 并发工具：可以用于并发测试。
`CyclicBarrier cyclicBarrier = new CyclicBarrier(n) `假设开启n个线程，每个线程内部都调用`cyclicBarrier.await();`方法，调用`await()`方法的参与者都会进行等待，直到所有的参与者都调用`await()`方法后，所有线程从 `await()` 返回并继续后续逻辑。。
6. 线程池 awaitTermination() 方法：使用这个方法的前提是必须先关闭线程池，如调用shutdown()方法。可以保证线程池中的任务全部执行完毕。
7. 管道通信：利用PipedWriter和PipedReader或者PipedInputStream和PipedOutputStream来进行通信，前者面向字符流，后者面向字节流，成对出现。

----------

#### synchronized关键字
- 同步普通方法，锁的是当前对象。
- 同步静态方法，锁的是当前 Class 对象。
- 同步块，锁的是()中的对象。

实现原理：JVM是通过进入或者退出Monitor对象监视器来实现同步的。进入前调用monitor.enter指令，退出时调用monitor.exit指令。每一个对象都有一个Monitor对象监视器，所以说每个对象都能作为锁来实现同步。


#### 分布式锁
①. 基于DB的唯一索引：可以创建一张表，将其中的某个字段设置为唯一索引，当多个请求过来的时候只有新建记录成功的请求才算获取到锁，当使用完毕删除这条记录的时候即释放锁。

>存在的问题:

>- 数据库单点问题，挂了怎么办？
- 不是重入锁，同一进程无法在释放锁之前再次获得锁，因为数据库中已经存在了一条记录了。
- 锁是非阻塞的，一旦 insert 失败则会立即返回，并不会进入阻塞队列只能下一次再次获取。
- 锁没有失效时间，如果那个进程解锁失败那就没有请求可以再次获取锁了。

>解决方案:

>- 数据库切换为主从，不存在单点。
- 在表中加入一个同步状态字段，每次获取锁的是加 1 ，释放锁的时候-1，当状态为 0 的时候就删除这条记录，即释放锁。
- 非阻塞的情况可以用 while 循环来实现，循环的时候记录时间，达到 X 秒记为超时，break。
- 可以开启一个定时任务每隔一段时间扫描找出多少 X 秒都没有被删除的记录，主动删除这条记录。






②. 基于ZK的临时有序节点: 使用watch机制监听相邻的最小子节点的删除事件，这样可以避免"羊群效应"（一次性将全部的子节点唤醒去竞争锁）。删除子节点为释放锁，下一个相邻的子节点获取锁。

③. 基于Redis的NX EX参数：使用setNX(key) setEX(timeout)命令，只有在该key不存在的时候创建这个key，就相当于获得了锁。由于有超时的限制，过了规定时间会自动删除，这样可以避免死锁。

#### ReentrantLock
这一块可以去了解一下：[ReentrantLock源码解读](https://www.hcyhj.cn/2018/11/24/reentrantlock/index.html "ReentrantLock源码解读")。

1. 支持重入锁：判断获取锁的线程是否为当前线程，如果是，则将状态+1。释放锁时，则将状态-1，直到状态值为0时，完全释放锁。
2. 分为公平锁与非公平锁：非公平锁的吞吐量比公平锁高很多。公平锁需要按内部维护的一个队列按次序来获取锁，这样会造成大量的上下文切换。而非公平锁则是采用竞争的方式去获取锁，可能同一个线程会多次获取到执行权限，这样便会减少上下文的切换。


----------
