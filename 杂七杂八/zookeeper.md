## Zookeeper介绍
### 2PC ??
### CAP ??
### 中心化与去中心化？？
### 三态 ？？

### Zookeeper介绍
1. 同级节点的唯一性
2. 临时节点和持久化节点（临时节点的生命周期与session保持一致）
3. 临时节点下不能有子节点
4. 有序节点特性

数据模型：创建节点是按文件结构的方式创建的（类比~）。

数据的版本号是通过乐观锁去实现的

### 集群
server.id = ip.port.port
myid可以自定义，但是每个zookeeper的配置的不能相同。

第一个端口用来内部通讯===>同步，第二个端口用来选举leader

### ACL（Access Control List）

### zookeeper的应用场景
1. 注册中心
2. 配置中心 （应用的配置文件可以放上面，通过watch机制推送改变的配置）
3. 负载均衡 选举master(可以利用有序节点)

### zookeeper的几种身份
1. leader
2. follower
3. obsever : 涉及到选举，不参与投票。

## 深入分析zookeeper的实现原理

### Zab协议

选举流程：

1. epoch 最大优先
2. epoch相等，zxid 优先（高位为epoch，低位为事务id，每次高位+1，低位都从0开始计数）
3. 上面两个都相等，最后判断server.id  