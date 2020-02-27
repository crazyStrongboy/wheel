## 搭建

### Docker环境的安装

1. 准备三台CentOS7的机器。
2. 安装Docker环境，测试使用的是`19.03.6`版本。

### 修改hosts

```shell
#主节点：
sudo hostnamectl set-hostname m

#从节点w1：
sudo hostnamectl set-hostname w1

#从节点w2：
sudo hostnamectl set-hostname w2

vi /etc/hosts

192.168.31.168 m
192.168.31.169 w1
192.168.31.170 w2
```



###  系统基础前提配置

```shell
# (1)关闭防火墙
systemctl stop firewalld && systemctl disable firewalld

# (2)关闭selinux
setenforce 0
sed -i 's/^SELINUX=enforcing$/SELINUX=permissive/' /etc/selinux/config

# (3)关闭swap
swapoff -a
sed -i '/swap/s/^\(.*\)$/#\1/g' /etc/fstab

# (4)配置iptables的ACCEPT规则
iptables -F && iptables -X && iptables -F -t nat && iptables -X -t nat && iptables -P FORWARD ACCEPT

# (5)设置系统参数
cat <<EOF >  /etc/sysctl.d/k8s.conf
net.bridge.bridge-nf-call-ip6tables = 1
net.bridge.bridge-nf-call-iptables = 1
EOF

sysctl --system
```



### Installing kubeadm, kubelet and kubectl

##### 配置yum源

```shell
cat <<EOF > /etc/yum.repos.d/kubernetes.repo
[kubernetes]
name=Kubernetes
baseurl=http://mirrors.aliyun.com/kubernetes/yum/repos/kubernetes-el7-x86_64
enabled=1
gpgcheck=0
repo_gpgcheck=0
gpgkey=http://mirrors.aliyun.com/kubernetes/yum/doc/yum-key.gpg
       http://mirrors.aliyun.com/kubernetes/yum/doc/rpm-package-key.gpg
EOF
```

##### 安装kubeadm&kubelet&kubectl

```shell
yum install -y kubeadm-1.14.0-0 kubelet-1.14.0-0 kubectl-1.14.0-0
```

##### docker和k8s设置同一个cgroup

```shell
# docker
vi /etc/docker/daemon.json
    "exec-opts": ["native.cgroupdriver=systemd"],
    
systemctl restart docker
    
# kubelet，这边如果发现输出directory not exist，也说明是没问题的，大家继续往下进行即可
sed -i "s/cgroup-driver=systemd/cgroup-driver=cgroupfs/g" /etc/systemd/system/kubelet.service.d/10-kubeadm.conf
	
systemctl enable kubelet && systemctl start kubelet
```



###  proxy/pause/scheduler等国内镜像

##### 查看kubeadm使用的镜像：kubeadm config images list

>k8s.gcr.io/kube-apiserver:v1.14.0
>k8s.gcr.io/kube-controller-manager:v1.14.0
>k8s.gcr.io/kube-scheduler:v1.14.0
>k8s.gcr.io/kube-proxy:v1.14.0
>k8s.gcr.io/pause:3.1
>k8s.gcr.io/etcd:3.3.10
>k8s.gcr.io/coredns:1.3.1

##### 解决国外镜像不能访问的问题

- 创建kubeadm.sh脚本，用于拉取镜像/打tag/删除原有镜像

```shell
#!/bin/bash

set -e

KUBE_VERSION=v1.14.0
KUBE_PAUSE_VERSION=3.1
ETCD_VERSION=3.3.10
CORE_DNS_VERSION=1.3.1

GCR_URL=k8s.gcr.io
ALIYUN_URL=registry.cn-hangzhou.aliyuncs.com/google_containers

images=(kube-proxy:${KUBE_VERSION}
kube-scheduler:${KUBE_VERSION}
kube-controller-manager:${KUBE_VERSION}
kube-apiserver:${KUBE_VERSION}
pause:${KUBE_PAUSE_VERSION}
etcd:${ETCD_VERSION}
coredns:${CORE_DNS_VERSION})

for imageName in ${images[@]} ; do
  docker pull $ALIYUN_URL/$imageName
  docker tag  $ALIYUN_URL/$imageName $GCR_URL/$imageName
  docker rmi $ALIYUN_URL/$imageName
done
```

- 运行脚本和查看镜像

```shell
# 运行脚本
sh ./kubeadm.sh

# 查看镜像
docker images
```



###  kube init初始化master

##### kube init流程

```shell
01-进行一系列检查，以确定这台机器可以部署kubernetes

02-生成kubernetes对外提供服务所需要的各种证书可对应目录
/etc/kubernetes/pki/*

03-为其他组件生成访问kube-ApiServer所需的配置文件
    ls /etc/kubernetes/
    admin.conf  controller-manager.conf  kubelet.conf  scheduler.conf
    
04-为 Master组件生成Pod配置文件。
    ls /etc/kubernetes/manifests/*.yaml
    kube-apiserver.yaml 
    kube-controller-manager.yaml
    kube-scheduler.yaml
    
05-生成etcd的Pod YAML文件。
    ls /etc/kubernetes/manifests/*.yaml
    kube-apiserver.yaml 
    kube-controller-manager.yaml
    kube-scheduler.yaml
	etcd.yaml
	
06-一旦这些 YAML 文件出现在被 kubelet 监视的/etc/kubernetes/manifests/目录下，kubelet就会自动创建这些yaml文件定义的pod，即master组件的容器。master容器启动后，kubeadm会通过检查localhost：6443/healthz这个master组件的健康状态检查URL，等待master组件完全运行起来

07-为集群生成一个bootstrap token

08-将ca.crt等 Master节点的重要信息，通过ConfigMap的方式保存在etcd中，工后续部署node节点使用

09-最后一步是安装默认插件，kubernetes默认kube-proxy和DNS两个插件是必须安装的
```

##### 初始化master节点

官网：<https://kubernetes.io/docs/reference/setup-tools/kubeadm/kubeadm/>

`注意`：**此操作是在主节点上进行**

```
# 本地有镜像
kubeadm init --kubernetes-version=1.14.0 --apiserver-advertise-address=192.168.31.168 --pod-network-cidr=10.244.0.0/16
【若要重新初始化集群状态：kubeadm reset，然后再进行上述操作】
```

此操作后，记得保存好最后kubeadm join的信息。

```shell
kubeadm join 192.168.31.168:6443 --token v9w85s.y44nr6ophcx8dejy \
    --discovery-token-ca-cert-hash sha256:ee37d60a38881bf8b8a01025100a0dc42627a63047cdf6b0aadf91cac60ff63f 

```



##### 根据日志提示

```shell
mkdir -p $HOME/.kube
sudo cp -i /etc/kubernetes/admin.conf $HOME/.kube/config
sudo chown $(id -u):$(id -g) $HOME/.kube/config
```

此时kubectl cluster-info查看一下是否成功

##### 查看pod验证一下

等待一会儿，同时可以发现像etc，controller，scheduler等组件都以pod的方式安装成功了

`注意`：coredns没有启动，需要安装网络插件

> kubectl get pods -n kube-system

##### 健康检查

> curl -k https://localhost:6443/healthz



### 部署calico网络插件

> 选择网络插件：<https://kubernetes.io/docs/concepts/cluster-administration/addons/>
>
> calico网络插件：<https://docs.projectcalico.org/v3.9/getting-started/kubernetes/>

> `calico，同样在master节点上操作`

```
# 在k8s中安装calico
kubectl apply -f https://docs.projectcalico.org/v3.9/manifests/calico.yaml

# 确认一下calico是否安装成功
kubectl get pods --all-namespaces -w
```



### kube join(从节点加入)

#### 执行join指令

执行上面主节点初始化完成后的记录信息，在w1和w2中执行(这里每个人初始化的都不一样)：

> kubeadm join 192.168.31.168:6443 --token v9w85s.y44nr6ophcx8dejy \
>     --discovery-token-ca-cert-hash sha256:ee37d60a38881bf8b8a01025100a0dc42627a63047cdf6b0aadf91cac60ff63f 

#### 检查是否加入集群

> kubectl get nodes

有如下日志表示搭建成功：

```
NAME   STATUS   ROLES    AGE     VERSION
m      Ready    master   7m35s   v1.14.0
w1     Ready    <none>   116s    v1.14.0
w2     Ready    <none>   74s     v1.14.0
```



### 简单的测试

#### 定义一个简单的nginx pod文件

比如pod_nginx_rs.yaml

```yml
cat > pod_nginx_rs.yaml <<EOF
apiVersion: apps/v1
kind: ReplicaSet
metadata:
  name: nginx
  labels:
    tier: frontend
spec:
  replicas: 3
  selector:
    matchLabels:
      tier: frontend
  template:
    metadata:
      name: nginx
      labels:
        tier: frontend
    spec:
      containers:
      - name: nginx
        image: nginx
        ports:
        - containerPort: 80
EOF
```

#### 根据pod_nginx_rs.yml文件创建pod

> kubectl apply -f pod_nginx_rs.yaml

#### 查看Pod

> kubectl get pods
> kubectl get pods -o wide
> kubectl describe pod nginx

#### 感受通过rs将pod扩容

> kubectl scale rs nginx --replicas=5
> kubectl get pods -o wide

#### 删除pod

> kubectl delete -f pod_nginx_rs.yaml
