

#### http和https

1. https协议需要到ca申请证书，一般免费证书较少，因而需要一定费用。
2. http是超文本传输协议，信息是明文传输，https则是具有安全性的ssl加密传输协议。
3. http和https使用的是完全不同的连接方式，用的端口也不一样，前者是80，后者是443。
4. http的连接很简单，是无状态的；HTTPS协议是由SSL+HTTP协议构建的可进行加密传输、身份认证的网络协议，比http协议安全。

##### https的流程

1. 在浏览器中输入一个https地址，连接服务器的443端口。
2. 服务端会返回对应证书的公钥，并且会额外返回颁发机构，过期时间等等信息。
3. 客户端解析证书，验证证书是否有效。（数字签名，对正文进行hash得到摘要，用CA私钥加密，到客户端用CA公钥解密，得到值h1,并对正文进行hash得到h2，比较h1与h2是否相等）
4. 传送加密信息，这里传送的是用证书加密后的随机值，目的就是让服务端得到这个值，后面服务端和客户端通信就是用这个值来加密数据的。
5. 服务端解密信息，得到客户端传来的随机值，然后把内容通过该值进行对称加密。
6. 把加密后的信息返回给客户端
7. 客户端通过之前的随机值进行解密。



#### 用户量很大，但是很多都是僵尸用户，常用保活就行，要针对高频用户分配多个goroutine该怎么去处理？

使用linux epoll机制即可。

 

#### cgo和普通函数的区别？

调用`runtime.asmcgocall`时会切换到go栈，（Go使用的是分段栈，在不够用时会进行动态增长。然而C函数不使用分段栈技术，并且假设栈是足够大的，因此调用cgo时使用的是m的g0栈，g0栈是固定的8K大小，`mp->g0 = runtime·malg(8192)`，这样相对来说会比较安全），在调用返回时，将会切换栈到`m.currg`的栈并且返回给`runtime.cgocall`。

##### 进入系统调用

Go的运行时库对系统调用作了特殊处理，所有涉及到调用系统调用之前，都会先调用runtime.entersyscall，而在出系统调用函数之后，会调用runtime.exitsyscall。这样做原因跟调度器相关，目的是始终维持GOMAXPROCS的数量，当进入到系统调用时，runtime.entersyscall会将P的M剥离并将它设置为PSyscall状态，告知系统此时其它的P有机会运行，以保证始终是GOMAXPROCS个P在运行。

runtime.entersyscall函数会立刻返回，它仅仅是起到一个通知的作用。那么这跟cgo又有什么关系呢？这个关系可大着呢！在执行cgo函数调用之前，其实系统会先调用runtime.entersyscall。这是一个很关键的处理，Go把cgo的C函数调用像系统调用一样独立出去了，不让它影响运行时库。这就回答了前面提出的第二个问题：Go中的goroutine都是协作式的，运行到调用runtime库时就有机会进行调度。然而C函数是不会与Go的runtime做这种交互的，所以cgo的函数不是一个协作式的，那么如何避免进入C函数的这个goroutine“失控”？答案就在这里。将C函数像处理系统调用一样隔离开来，这个goroutine也就不必参与调度了。而其它部分的goroutine正常的运行不受影响。

##### 退出系统调用

退出系统调用跟进入系统调用是一个相反的过程，runtime.exitsyscall函数会查看当前仍然有可用的P，则让它继续运行，否则这个goroutine就要被挂起了。

对于cgo的代码也是同样的作用，出了cgo的C函数调用之后会调用runtime.exitsyscall。



#### Redis的持久化AOF的优势

1. 能够给数据带来更高的安全性，数据的同步操作都是异步完成的。

2. 对日志的写入是采用的append模式，因此在写入过程中出现宕机问题，也不会影响前面的数据。如果写到一半出问题了，在redis重启时会通过工具redis-check-aof去解决数据一致性问题。

3. AOF文件越来越大，定期对AOF文件进行重写，达到压缩的目的。更小的AOF文件能够被Redis更快的加载。

   - 超时的数据不再写入文件

   - 进行命令的合并





#### Redis集群



#### mongodb分片

**shard**：每个分片是整体数据的一部分子集。每个分片都可以部署为副本集。

**mongos**：充当查询路由器，提供客户端应用程序与分片集群之间的接口。

**config servers**：配置服务器存储集群的元数据和配置。

**shared key**：分片键，一个集合有且只能有一个分片键，确定之后就不能更改，分片键的好坏直接影响到整个集群的性能 。

- 基于hash的分片：等值查询效率高
- 基于ranged的分片：范围查询效率高



#### mongo和redis、Memcached对比

**memcache**：可以利用多核的优势，单实例吞吐量极高，但是不支持持久化操作。数据结构也比较单一，只支持简单的key/value形式，目前redis正在取代于它。

**redis**：支持多种数据结构，string、list、hash、set、zset。支持持久化，支持master-slave机制，单线程请求。只能使用单线程，性能受限于CPU瓶颈。并且不适宜在不同的数据集之间建立关系，不适合进行模糊查询。热点数据用redis比较合适。

**mongo**：文档型数据库，可以存放xml,json,bson类型的数据。支持丰富的数据表达，索引，查询语句也很丰富。大数据量的情况下用mongo比较合适。



#### consul与zookeeper对比

**consul**：简单的客户端，支持http协议。业务的可用状态是由agent来维护的。agent不能正常工作，就没办法确定服务的正常状态。利用gossip协议进行分布式健康检测。支持任意存储，不仅仅是简单的K/V存储，还可以支持DNS接口，开发人员可以建立完整的服务发现解决方案。

**zookeeper**：胖客户端，需要定制。利用临时节点机制，业务启动创建临时节点，节点在服务就在。依赖jvm。

**etcd**：利用TTL机制，业务服务启动时创建键值对，定时更新ttl，ttl过期则服务不可用。





#### 网络安全

#### 加密协议

#### Raft协议



#### Kafka备份和容灾

##### 备份

topic这一类的消息，可能有多个partition，然后每个partition会有多个副本，这里副本会选举出一个leader，然后读取和写入都是通过leader来实现的，这就实现了强一致性的CP。如果leader出了问题，则会从isr列表中重新选举出一个leader。

**ISR列表**：leader会扫描ISR列表（in sync replica）中的follower。如果一个follower宕机了，leader将会将其从ISR列表中移除掉，判断标准是follow复制的消息数落后于leader的条数的预期值。这个值由属性`replica.lag.max.messages`和`replica.lag.time.max.ms`，前者控制消息条数，后者控制多久没进行复制消息了。

**消息的复制**：一条消息只有被ISR列表中的所有follower都复制过去后才会被认为已经提交，这样避免数据的丢失，对于producer而言，只用保证达到配置`request.required.acks`数的消息被复制后，就认为该消息已经commit了。

##### 容灾

brocker集群通过向zookeeper注册临时节点/Controller，来选举出Controller，并且每个broker都会监听该节点，通过临时节点的变动来决定是否进行Controller的选举。Controller宕机，触发其他broker进行Controller的重新选举。如果宕机的broker上有leader partition，则会重新从ISR选举出对应partition的leader，将其信息写入broker的state节点。



#### Kafka与RabbitMQ对比

kafka不支持优先级队列，延迟队列，死信队列，重试队列，消息追踪。但是支持消息回溯，通过offset进行偏移量的设置，重新消费消息。单分区内支持顺序性。消费消息只支持拉模式。支持持久化。特定协议。



rabbitMQ支持优先级队列，延迟队列，死信队列，重试队列，消息追踪。不支持消息回溯，基本不支持消息的顺序性。消费模式支持推和拉两种模式。支持持久化。本身由AMQP协议实现，同时支持MQTT、STOMP协议。