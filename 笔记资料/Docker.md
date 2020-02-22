### 什么是Docker?

Docker是一个供我们开发、发布、运行app的开放平台，它能够帮助我们轻松的从基础实施上隔离应用。可以用Docker实现像管理程序一样去管理我们的基础设施。



### 镜像（Docker Hub），容器

一个镜像Image可以在不同的配置下运行，从而对应多个容器Containers。Container可以共享物理资源。Container底层是一个最原始的linux操作系统。



### 虚拟化技术，容器化技术

1. 虚拟化技术比容器化技术多一个OS层，需要向物理机申请更多的资源。



### Docker指令

1. docker images：查询所有的镜像
2. docker build： 创建镜像
3. docker ps -a： 列出所有的容器，docker  ps，列出正在运行的容器
4. docker pull ：拉取镜像
5. docker push：推送镜像到hub
6. docker tag [image] [alias]：命名别名
7. docker run：运行镜像，生成容器
8. docker rmi：删除镜像
9. docker rm：删除容器
10. docker exec -it xx /bin/bash：进入容器
11. docker  rm -f  $(docker ps -aq)：移除全部容器
12. docker  network ls： 查看网卡
13. docker inspect bridge：查看桥接网络 
14. docker logs 【容器名或者ID】：查看日志
15. docker commit：由container生成image
16. docker network：操作网络相关的
17. docker volume ls：查看挂载卷





### Dockerfile

执行docker build dockerfile，生成image

```dockerfile
#Dockerfile
From ubuntu

Copy main helloworld

Run chmod 755 helloworld

CMD ["./helloworld"]
```







### Harbor

私有hub的搭建



### weaveworks/scope

进行容器的监控



### 网络通讯

#### Network Namespace：veth-pair 桥接模式，原理

#### Bridge

#### Host

#### Null

#### Overlay：多机网络通讯





### 数据持久化

docker -v 宿主机：容器



### percona-xtradb-cluster（mysql 集群）

1. docker pull percona/percona-xtradb-cluster:5.7
2. docker tag percona/percona-xtradb-cluster:5.7 pxc
3. 创建一个桥接网卡：docker network create  --subnet 172.19.0.0/24 pxc-net
4. 创建几个volume：
   1. docker volume create v1
   2. docker volume create v2
   3. docker volume create v3
5. 创建容器：
   1. docker run -d -p 3301:3306 --name mysql01 -v v1:/var/lib/mysql --privileged -e MYSQL_ROOT_PASSWORD=123456 -e XTRABACKUP_PASSWORD=123456  --network pxc-net -e CLUSTER_NAME=cluster-demo --ip 172.19.0.2 pxc
   2. docker run -d -p 3302:3306 --name mysql02 -v v2:/var/lib/mysql --privileged -e MYSQL_ROOT_PASSWORD=123456 -e XTRABACKUP_PASSWORD=123456  --network pxc-net -e CLUSTER_NAME=cluster-demo --ip 172.19.0.3  -e CLUSTER_JOIN=mysql01 pxc
   3. docker run -d -p 3303:3306 --name mysql03 -v v3:/var/lib/mysql --privileged -e MYSQL_ROOT_PASSWORD=123456 -e XTRABACKUP_PASSWORD=123456  --network pxc-net -e CLUSTER_NAME=cluster-demo --ip 172.19.0.4  -e CLUSTER_JOIN=mysql01  pxc



### haproxy

```
global
	#工作目录，这边要和创建容器指定的目录对应 
	chroot /usr/local/etc/haproxy 
	#日志文件 
	log 127.0.0.1 local5 info 
	#守护进程运行 
	daemon
	
defaults 
	log global 
	mode http 
	#日志格式 
	option httplog 
	#日志中不记录负载均衡的心跳检测记录 
	option dontlognull 
	#连接超时（毫秒） 
	timeout connect 5000 
	#客户端超时（毫秒） 
	timeout client 50000 
	#服务器超时（毫秒） 
	timeout server 50000 
	#监控界面 
	listen admin_stats 
	#监控界面的访问的IP和端口 
	bind 0.0.0.0:8888 
	#访问协议 
	mode http 
	#URI相对地址 
	stats uri /dbs_monitor 
	#统计报告格式 
	stats realm Global\ statistics 
	#登陆帐户信息 
	stats auth admin:admin 
	#数据库负载均衡 
	listen proxy-mysql 
	#访问的IP和端口，haproxy开发的端口为3306 
	#假如有人访问haproxy的3306端口，则将请求转发给下面的数据库实例 
	bind 0.0.0.0:3306 
	#网络协议 
	mode tcp 
	#负载均衡算法（轮询算法） 
	#轮询算法：roundrobin 
	#权重算法：static-rr 
	#最少连接算法：leastconn 
	#请求源IP算法：source 
	balance roundrobin 
	#日志格式 
	option tcplog 
	#在MySQL中创建一个没有权限的haproxy用户，密码为空。
	#Haproxy使用这个账户对MySQL数据库心跳检测 
	option mysql-check user haproxy 
	server MySQL_1 172.19.0.2:3306 check weight 1 maxconn 2000 
	server MySQL_2 172.19.0.3:3306 check weight 1 maxconn 2000 
	server MySQL_3 172.19.0.4:3306 check weight 1 maxconn 2000 
	#使用keepalive检测死链 
	option tcpka
```

- 创建容器：docker run -it -d -p 8888:8888 -p 3306:3306 -v /tmp/haproxy:/usr/local/etc/haproxy --name haproxy01 --privileged --net=pxc-net haproxy 
- 启动haproxy：
  - docker exec -it haproxy01 bash 
  - haproxy -f /usr/local/etc/haproxy/haproxy.cfg 

### 容器编排工具

1. 单机编排：docker compose
2. 多机编排：docker swarm、 mesos、 kubernets