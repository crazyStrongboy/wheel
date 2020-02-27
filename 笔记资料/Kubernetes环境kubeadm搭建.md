### Pod

pod的生命周期：

| Value       | Description                                                  |
| :---------- | :----------------------------------------------------------- |
| `Pending`   | The Pod has been accepted by the Kubernetes system, but one or more of the Container images has not been created. This includes time before being scheduled as well as time spent downloading images over the network, which could take a while. |
| `Running`   | The Pod has been bound to a node, and all of the Containers have been created. At least one Container is still running, or is in the process of starting or restarting. |
| `Succeeded` | All Containers in the Pod have terminated in success, and will not be restarted. |
| `Failed`    | All Containers in the Pod have terminated, and at least one Container has terminated in failure. That is, the Container either exited with non-zero status or was terminated by the system. |
| `Unknown`   | For some reason the state of the Pod could not be obtained, typically due to an error in communicating with the host of the Pod. |

1. 每个pod中都有一个pause container容器，后面所有加进来的容器都会链接到这个容器中。所以同一个pod中的所有container共享同一个网络。
2. pod之间的通讯
   - 不同node上的pod
   - 同一台node上的pod

查询pod：

- `kubectl get pods --all-namespaces`查询所有名称空间下的pod
- `kubectl get pods  -n  xxx` 查询xxx名称空间下的pod，不加上-n参数表示查询默认空间
- `kubectl describe pod pod-name -n xxx`查询pod的详细信息

创建pod：`kubectl apply -f nginx-pod.yaml`

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



### ReplicaController

### ReplicaSet

### Deployment

一般与ReplicaSet配合使用

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







多个replicas的mysql如何挂载volume?

