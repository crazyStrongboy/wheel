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





### Dockerfile

执行docker build dockerfile，生成image



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