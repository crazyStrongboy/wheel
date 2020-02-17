## 索引
索引是为了加速对表中的数据行的检索而创建的一种分散储存的数据结构，索引是由插拔式的存储引擎来实现的，例如InnoDB，MyISAM等等，每张表可以设定特定的存储引擎。

### 为什么要用索引
1. 索引能够帮助我们减少存储引擎需要扫描的数据量
2. 索引可以把随机IO变成顺序IO
3. 索引可以帮助我们在分组，排序等操作时，避免使用临时表

### 为什么数据库索引要用B+tree?
- 普通的Tree : 可能出现一个分支过长，检索过深。
- 平衡二叉树：一个节点只能存储一个索引（没有利用到磁盘IO的数据交换特性），然后检索也会很深。
- 多路平衡查找tree: 数据区和索引存储在一个节点上。
- B+tree（加强版多路平衡查找树）：叶子节点才存储数据区，其他节点仅记录索引，子节点的引用和操作锁的一些功能。

B+tree（加强版多路平衡查找树）: 主键会指向叶子节点去获取数据。叶子节点是顺序排列的，且相邻节点具有引用顺序关系。这样磁盘的读写能力更强。它拥有B-tree的优势，扫表，库以及排序能力更强，还具有稳定性~

### 主键索引与辅助索引
- MyISAM引擎的索引和数据是分文件存储的。主键索引和辅助索引均指向数据的地址值。
- InnoDB引擎的索引和数据是存储在一个文件的。辅助索引依赖主键索引（主键索引指向数据的地址值），这样数据迁移时无需关注辅助索引。

### 联合索引选择原则（单列索引就是特殊的联合索引。）

- 离散性越高，选择性越好。就是重复性或者hash后碰撞的几率要低。
- 最左匹配原则。经常用的字段优先。
- 最少空间原则。（字段不易过大）。

### 覆盖索引
如果查询列可以直接通过索引节点中的关键字返回，则称该索引为覆盖索引。覆盖索引可以减少数据IO，将随机IO变成顺序IO，可提高查询性能。（不用去叶子节点中获取数据了）

### MYSQL基于索引优化
- 建立索引的长度能小则小
- 索引数目不易过多
- 少用like,有可能用不上索引，有可能用得上
- 少用not in，  <>
- order by 可用到索引
- 少用select *
- 联合索引不匹配最左原则，也用不到索引
- 最左精确匹配，范围匹配另一侧也有可能用到索引
- 最左范围匹配则一定用不到索引


### 磁盘顺序IO 随机IO
顺序IO比随机IO要快，因为它避免了昂贵的寻道时间以及旋转延迟的开销。顺序IO更在意带宽，而随机IO更在意系统单位时间内处理IO的数量。

### 聚集索引
聚集索引指的是数据库表中数据的物理顺序与键值的逻辑（索引）顺序相同。一个表只能有一个聚集索引。聚集索引比非聚集索引有更快的检索速度。


## 存储引擎
> -  插拔式的插件方式
> 
> -  存储引擎建立在表上面，每个表有特定的存储引擎
> 
> - 任何存储引擎都会在数据区产生一个frm的表结构定义文件

### CSV存储引擎
数据存储为CSV文件

特点：

- 不能定义索引，列定义必须为NOT NULL，不能设置自增（不适合大表和数据的在线处理）
- 可直接编辑CSV文件进行修改，然后调用flush table xx生效（安全性极低）

应用场景：

- 数据的快速导入导出
- 表格直接转换成为CSV文件

### Archive存储引擎
压缩协议进行数据的存储，数据存储为ARZ文件格式

特点：

- 只支持insert和select操作
- 只允许自增ID列建立索引
- 行级锁
- 不支持事务
- 数据占用磁盘空间较少

应用场景：

- 日志系统
- 大量的设备数据采集

### Memory存储引擎
数据都是存储在内存中，IO效率比其他引擎高得多，服务重启数据丢失，内存数据表默认存储大小16M。

特点：

- 支持hash,B-tree索引
- 字段固定长度varchar（32）
- 不支持大数据存储类型字段，如blog,text
- 表级锁

应用场景：

- 等值查找热度比较高的数据
- 查询结果内存中进行计算，作为临时表存储要计算的数据（不符合要求时会采用Myisam引擎作为临时表的存储引擎）

### Myisam存储引擎
Mysql5.5版本之前的默认存储引擎，系统临时表也会用到Myisam存储引擎。

特点：

- select count(*) from table无需用到全表扫描，因为它会记录一张表的行数，count时直接返回行数。
- 数据和索引分开储存
- 表级锁
- 不支持事务

### Innodb存储引擎
Mysql5.5版本以后的默认存储引擎

- 支持事务
- 行级锁
- 聚集索引（主键索引）方式进行数据存储
- 支持外键
- 索引与数据存储在一个文件中


#### [MyISAM和InnoDB的区别](https://mp.weixin.qq.com/s/FUXPXKfKyjxAvMUFHZm9UQ)？
- MyISAM是表级锁，InnoDB是行级锁。
- MyISAM读很快。InnoDB写效率高。
- MyISAM索引与数据分开存储，InnoDB索引与数据存储在一个文件中。
- MyISAM的索引叶子存储指针，主键索引与普通索引无太大区别；InnoDB的聚集索引存储数据行本身，普通索引存储主键，InnoDB一定有且只有一个聚集索引
- InnoDB建议使用趋势递增整数作为PK，而不宜使用较长的列作为PK
- InnoDB对一行数据的读不会加锁，实际上读的是副本（快照）。

## Mysql查询优化

### Mysql客户端与服务端的通信
Mysql客户端与服务端之间的通信采用半双工的方式。

限制：

- 客户端一旦开始发送消息，另一端要接受完消息才能开始响应。
- 客户端一旦开始接收指令，就没办法发送指令。

#### 查询通信状态
show full processlist/show processlist（可以通过kill {id}的形式将连接杀掉）

状态：

- Sleep:线程正在等待客户端发送数据
- Query:连接线程正在执行查询
- Locked:线程正在等待表锁的释放
- Sorting result:线程正在对结果进行排序
- Sending result:向请求端返回数据

### 查询缓存
工作原理：

- 缓存Select的结果集以及select语句
- 新的 select语句会先去查询缓存，判断是否有可用的结果集

判断标准：与缓存的SQL是否完全一样，区分大小写。

一旦一个表中有一个数据修改，缓存该表的所有sql都会失效，所以一般用来数据生成之后就不常改变的业务，减少资源的浪费。

####  参数值设置
query\_cache\_type

- 0：不启用缓存
- 1：启用查询缓存,缓存所有查询语句，除非有SQL_ NO_ CACHE修饰
- 2：启用查询缓存，仅缓存有参数SQL_ CACHE修饰的sql语句

query\_cache\_size：允许缓存的最大容量，必须是1024的整数倍
	
query\_cache\_limit：允许单条数据的最大值

show status like 'Qcache%' 命令可以查询缓存情况

### 查询处理优化

三个阶段：

- 解析SQL：将sql语句解析成语法树
- 预处理阶段：根据mysql语法规则进一步检查语法树的合法性
- 查询优化器： 找到最优的执行计划

>Mysql的查询优化器是基于成本计算的原则，他会尝试各种执行计划，数据抽样的方式进行试验（随机的读取一个4k的数据块进行分析）

#### 执行计划Id
1. id相同，执行顺序由上往下
2. id不同，如果是子查询，id的序号会递增，id值越大优先级越高，越先被执行
3. id相同又不同的情况，id相同被分为同一组，从上往下顺序执行；在所有组中，id值越大，优先级越高，越先执行。

#### 执行计划type
访问类型，sql查询优化中的一个很重要的指标，结果值从好到怀依次是：

**system>const>eq_ref>ref>range>index>all**

- system:表只有一行记录，const类型的特例
- const:通过索引依次就找到了，一般用于primary key或者unique索引
- eq_ref：唯一索引扫描，
- ref：非唯一索引扫描，返回匹配单个值的所有行
- range:只检索给定返回的行，使用一个索引来选择行
- index：Full index scan，进行索引全部扫描
- All: Full table scan,遍历全表找到匹配的行

#### 执行计划possible\_keys，keys,rows,filtered
- possible\_keys:查询过程中可能用到的索引
- keys:实际用到的索引，为NULL表示没用到索引
- rows:估算出要得到实际记录需要检索的行数
- filtered:返回结果的行占用读取行数的百分比，越大越好，表示命中率越高

#### 执行计划 Extra
1. Using filesort: mysql使用了一个外部的文件对内容进行了排序，而不是按表内的索引进行排序
2. Using temporary: 使用临时表保存中间结果，常见于group by和order by
3. Using index:相应的sql使用到了覆盖索引，避免了访问表的数据行
4. Using where： 用了where条件语句
5. select tables optimized away: 基于索引优化的min和max操作或者MYISAM存储引擎的select count(*),不必等到执行阶段再进行计算

### 返回客户端
- 有需要做缓存的先执行缓存操作
- 增量返回结果：开始生成第一条时，mysql就开始逐条返回结果数据。这样mysql服务器无需保存过多的数据，减小内存的浪费，用户体验感也会更好。

### 如何定位SQL执行慢
- 业务驱动
- 测试驱动
- 慢查询日志

慢查询日志基本操作：
>show variables like 'slow\_query\_log'
>
>set  global slow\_query\_log = on
>
>set global  slow\_query\_log\_file = '/var/lib/mysql/gupaoedu-slow.log'
>
>set global log\_queries\_not\_using\_indexes = on
>
>set global long\_query\_time = 0.1 (秒)

## 事务

### 事务的四大特性
- 原子性（Atomicity）：最小的工作单元，整个工作单元要么一次性成功，要么一次性失败
- 一致性（Consistency）：执行的结果必须完全符合预设规则
- 隔离性（Isolation）：一个事务在提交之前，对其他事务可见性的设定
- 持久性（Durability）：事务所做的修改会永远保存，数据不会丢失

### 隔离级别
1. Read Uncommitted-未提交读：**什么都不解决**
2. Read Committed提交读（Oracle默认级别）：**解决脏读**
3. Repeatable Read-可重复读（InnoDB默认级别）:**解决不可重复读，Innodb在这一级别不可能出现幻读**
4. Serializable串行读：**解决所有问题，包括幻读**

>脏读：读到了未提交的数据
>
>重复读：单个读取两次读取结果不一致
>
>幻读：区间读取两次结果不一致

## 锁

### mysql锁
- 行级锁：针对操作的行加锁，锁粒度最小，开销最大，并发度最高。行级锁分共享锁和排它锁。会出现死锁。
- 表级锁：最大粒度的锁，锁整个表，资源消耗较少，被大部分的引擎（MyISAM和InnoDB）所支持。并发度最低。不会出现死锁。
- 页级锁：介于上面两个锁之间，会出现死锁，并发度一般。表现为一次锁定相邻的一组记录。

### 共享锁（行锁 Shared Locks）
>又称为读锁，简称S锁，多个事务共享一把锁，都能够读取数据，不能修改。

加锁方式：select * from users WHERE id=1 LOCK IN SHARE MODE;
### 排它锁(行锁 Exclusive Locks)
>又称为写锁，简称X锁，无法与其他锁共存，一个事务获取该锁，其他事务均无法再获取该行的锁。**其他事务读取数据可来自于快照。**

加锁方式：

- delete / update / insert 默认加上X锁
- SELECT * FROM table_name WHERE ... FOR UPDATE

### 意向共享锁(表锁 Intension Shared Locks)与意向排它锁(表锁 Intension Exclusive Locks)
> 这两个锁是表级别的锁，一张表只能有一个这样的锁（IS和IX是InnoDB在数据操作前自动加上的，无需用户去干涉）。但是InnoDB的表级别的锁是将所有行均加锁来实现的，所以当事物进行锁表时，会检查这两种意向锁是否存在，若存在，则会立即返回，不能启动锁表。但是锁行时意向所是可以相互兼容的。

### 自增锁(Auto\_INC Locks)
> 针对自增列自增长的一个特殊的表级别的锁。默认从1开始，事务未提交，则未提交的ID永久消失。

查询：show variables like 'innodb_autoinc_lock_mode';

### 行锁的算法
> InnoDB的行锁主要是通过给索引上的索引项加锁来实现的，只有通过索引条件进行数据检索时,InnoDB才使用行锁，否则，InnoDB使用表锁。

表锁：lock tables xx read/write；
#### 记录锁(Record Locks)
>当sql执行按照唯一性（Primary key、Unique key）索引进行数据的检索时，查询条件等值匹配且查询的数据是存在，这时SQL语句加上的锁即为记录锁Record locks，锁住具体的索引项
#### 间隙锁(Gap Locks)
>临键锁可降级为间隙锁。锁住索引不存在的区间（左开右闭）
#### 临键锁（Next-key Locks）==> 行锁的默认算法(是记录数和间隙锁的组合)
>当sql执行按照索引进行数据的检索时,查询条件为范围查找（between and、<、>等）并有数据命中则此时SQL语句加上的锁为Next-key locks，锁住索引记录+区间（左开右闭原则）

### 死锁介绍
> 每个事务（2个及以上）均占有锁且等待对方的锁，会发生死锁。

怎样避免：

- 业务按固定的顺序去访问表和行
- 化整为零，将大事务拆成小事务
- 在同一个事务中，尽可能的多锁定一些资源
- 降低隔离级别
- 为表添加合适的索引

## MVCC
Mysql的MVCC是在Innodb引擎中支持的，Innodb为每行记录增加了三个字段：

- 6个字节的事务ID（DB\_TRX\_ID）
- 7字节的回滚指针（DB\_ROLL\_PTR）
- 隐藏的ID

**进行操作时，会给每个session都分配一个事务ID。**

MVCC有以下几个特点:

- 每行数据上都有数据行版本号：新增或者update时会改变这个版本号
- 每行数据上都有删除版本号：删除时会改变这个版本号

### Undo
> 进行更新操作后，还没commit，这样读取的行会存储一份到undo buffer中，作为快照，防止进行修改时排他锁限制后无法读取的情况，undo buffer最后会根据策略刷入硬盘。
### Redo（事务的提交）
> 在进行commit后，不会直接写入到数据库中，毕竟更新的数据在磁盘是不连续的，如果直接入库会导致多次寻址。这里我们redo帮我们解决这个问题，commit后数据行会被写入到redo buffer中，redolog是可以顺序写的，redo buffer最后会根据策略刷入硬盘中。

#### Redo的一些配置
- 可通过innodb\_log\_group\_home\_dir配置指定目录存储，{datadir}/ib\_logfile1&ib\_logfile2
- redo log的写入是循环写入的，数量innodb\_log\_files\_in\_group默认为2
- 最大存储量innodb_log\_file\_size 默认48M
- cache/buffer 中的buffer 池大小innodb_log\_buffer\_size默认16M
- 持久化策略，innodb\_flush\_log\_at\_trx\_commit 取值0：每秒都刷到硬盘 1：每次提交事务都刷到硬盘
2： 事务提交到buffer，过一秒后刷到硬盘

### rollback segmengt
>在Innodb中，undo log会被分为多个段，其中有个段就是回滚段，用来处理事务的回滚。

### 快照读
>SQL读取的数据是快照版本，也就是历史版本，普通的select就是快照读。读取的实际上是undo里面缓存的版本。
### 当前读
>SQL读取的是最新版本，通过锁机制来保证读取的数据其他事物无法进行修改。

例如：SELECT … LOCK IN SHARE MODE 、SELECT … FOR UPDATE 等等都是当前读。

## 其他信息（不定时补充）
### 数据库三范式
- 每一列都只能是单一的值，不能再切分
- 每一行都要有主键进行区分
- 每个表都不能包含其他表除了主键外的其他字段


### 查询配置文件
mysql --help 寻找配置文件的位置和加载顺序：

**Default options are read from the following files in the given order:
/etc/my.cnf /etc/mysql/my.cnf /usr/etc/my.cnf ~/.my.cnf**

mysql --help | grep -A 1 'Default options are read from the following
files in the given order'

### 最大连接数配置
- my.cnf : max\_connections(配置文件中)
- /etc/security/limits.conf  查询：ulimit -a（系统句柄数配置）
- /usr/lib/systemd/system/mysqld.service （mysql句柄数配置）



### 单工、半双工、双工

- 单工：数据单向传输
- 半双工：数据双向传输，但是不能同时传输
- 双工：数据可以双向同时传输

MySQL通讯时采用半双工，客户端发送指令，服务端进行数据的返回。插入和查询时需要控制语句的长度，发送指令时不支持拆分。



### 查询流程

![img](https://img2018.cnblogs.com/blog/1479022/201904/1479022-20190421120030242-451092172.jpg)