## 标签

### Pod

#### pod的生命周期

| Value       | Description                                                  |
| :---------- | :----------------------------------------------------------- |
| `Pending`   | The Pod has been accepted by the Kubernetes system, but one or more of the Container images has not been created. This includes time before being scheduled as well as time spent downloading images over the network, which could take a while. |
| `Running`   | The Pod has been bound to a node, and all of the Containers have been created. At least one Container is still running, or is in the process of starting or restarting. |
| `Succeeded` | All Containers in the Pod have terminated in success, and will not be restarted. |
| `Failed`    | All Containers in the Pod have terminated, and at least one Container has terminated in failure. That is, the Container either exited with non-zero status or was terminated by the system. |
| `Unknown`   | For some reason the state of the Pod could not be obtained, typically due to an error in communicating with the host of the Pod. |

#### Pod的网络

- 每个pod中都有一个pause container容器，后面所有加进来的容器都会链接到这个容器中。所以同一个pod中的所有container共享同一个网络。

- 不同pod直接通过网络插件进行通讯。

#### 查询pod

- `kubectl get pods --all-namespaces`查询所有名称空间下的pod
- `kubectl get pods  -n  xxx` 查询xxx名称空间下的pod，不加上-n参数表示查询默认空间
- `kubectl describe pod pod-name -n xxx`查询pod的详细信息

#### 创建pod

指令：`kubectl apply -f nginx-pod.yaml`

```yaml
apiVersion: v1
kind: Pod
metadata:
  name: nginx-pod
spec:
  containers:
  - name: nginx-container
    image: nginx
    ports:
    - containerPort: 80
```

#### Pod重启策略（restartPolicy）

- Always：容器失效就重启。默认值。
- OnFailure：非正常退出就重启。
- Never：永远不重启。

#### 静态Pod

由kubelet单独去管理的



### Controller

#### ReplicaController

#### ReplicaSet

#### DaemonSet

每个node上都会运行带有`kind:DaemonSet`的pod。适用场景如下：

- running a cluster storage daemon, such as `glusterd`, `ceph`, on each node.
- running a logs collection daemon on every node, such as `fluentd` or `filebeat`.
- running a node monitoring daemon on every node, such as [Prometheus Node Exporter](https://github.com/prometheus/node_exporter), [Flowmill](https://github.com/Flowmill/flowmill-k8s/), [Sysdig Agent](https://docs.sysdig.com/), `collectd`, [Dynatrace OneAgent](https://www.dynatrace.com/technologies/kubernetes-monitoring/), [AppDynamics Agent](https://docs.appdynamics.com/display/CLOUD/Container+Visibility+with+Kubernetes), [Datadog agent](https://docs.datadoghq.com/agent/kubernetes/daemonset_setup/), [New Relic agent](https://docs.newrelic.com/docs/integrations/kubernetes-integration/installation/kubernetes-installation-configuration), Ganglia `gmond`, [Instana Agent](https://www.instana.com/supported-integrations/kubernetes-monitoring/) or [Elastic Metricbeat](https://www.elastic.co/guide/en/beats/metricbeat/current/running-on-kubernetes.html).

#### Deployment

一般与ReplicaSet配合使用

#### Job

**Job对象通常用于运行那些仅需要执行一次的任务（例如数据库迁移，批处理脚本等等）**。通过Job对象创建运行的Pod具有高可靠性，因为Job Controller会自动重启运行失败的Pod（例如Pod所在Node重启或宕机）。

```yaml
apiVersion: batch/v1
kind: Job
metadata:
  name: pi
spec:
  template:
    spec:
      containers:
      - name: pi
        image: perl
        command: ["perl",  "-Mbignum=bpi", "-wle", "print bpi(2000)"]
      restartPolicy: Never
  # 容错次数
  backoffLimit: 4
```



#### CronJob

一个拥有定时执行任务能力的Job。其他和Job没有区别。

### Label

### Service

### Node

### Kubectl

### Ingress

### Namespace

1. 查询当前所有的名称空间：`kubectl get namespaces/ns`

eg:

```
NAME              STATUS   AGE
default           Active   2d20h
ingress-nginx     Active   2d2h
kube-node-lease   Active   2d20h
kube-public       Active   2d20h
kube-system       Active   2d20h
myns              Active   2d3h
```

其实说白了，命名空间就是为了隔离不同的资源，比如：Pod、Service、Deployment等。可以在输入命令的时候指定命名空间`-n`，如果不指定，则使用默认的命名空间：default。



2. 名称空间的创建：`kubectl apply -f myns-namespace.yaml`

```yaml
apiVersion: v1
kind: Namespace
metadata:
 name: myns
```

3. 创建指定名称空间的pod

在`metadata`中加上参数`  namespace: myns`即可，其他和创建pod流程一致。

```yaml
apiVersion: v1
kind: Pod
metadata:
  name: nginx-pod
  namespace: myns
spec:
  containers:
  - name: nginx-container
    image: nginx
    ports:
    - containerPort: 80
```





### Volume

- PV（PersistentVolume）的状态

Available：表示当前的pv没有被绑定

Bound：表示已经被pvc挂载

Released：pvc没有在使用pv, 需要管理员手工释放pv

Failed：资源回收失败

- PV（PersistentVolumeClaims）回收策略

Retain：表示删除PVC的时候，PV不会一起删除，而是变成Released状态等待管理员手动清理

Recycle：在Kubernetes新版本就不用了，采用动态PV供给来替代

Delete：表示删除PVC的时候，PV也会一起删除，同时也删除PV所指向的实际存储空间

`注意`：目前只有NFS和HostPath支持Recycle策略。AWS EBS、GCE PD、Azure Disk和Cinder支持Delete策略



一个pod可以对应多个pvc，pvc和pv之间是一一对应的关系。

### StorageClass



### ConfigMap

## 组件

### Scheduler

- 预选策略
- 优选策略

### Kube-apiserver

### Kubectl

### Kubelet

1. 每个node节点上都有的进程，用于处理master节点下发到当前节点的任务。
2. 管理pod以及容器，kubelet进程会在apiserver上注册节点的自身信息，包括资源的使用情况等等。



### Kubeadm

### 注册中心

ETCD

## K8s解决方案

### Rancher



## 部署策略

- 滚动更新：部分停止并更新，直到所有都更新完成
- 重新创建：停掉所有的老版本，再重新部署新版本
- 蓝绿部署：无需停止服务，风险较小
- 金丝雀部署【AB测试】：只更新部分，新老版本共存



## ServiceMesh



## Istio

CNCF：Cloud Native Computing Foundation



### Sidecar

Envoy：c++开发的一个高性能的代理组件





# 多个replicas的mysql如何挂载volume?