This is a simple rpc framework rudiment!

    Server:
        //1.创建一个注册中心
        RegistryCenter registry = new ZookeeperRegistryCenter();
        //2.创建一个rpc服务端
        RpcServer rpcServer = new RpcServer(registry,"127.0.0.1:8080");
        //3.注册服务
        Hello hello = new HelloImpl();
        rpcServer.bind(hello);
        //4.发布服务
        rpcServer.publish();
    
    
    Client:
        //1.创建一个服务发现节点
        ServiceDiscovery discovery = new ZookeeperServiceDiscovery();
        //2.创建一个远程调用实例
        RpcClientProxy rpcClient = new JdkRpcClientProxy(discovery);
        Hello hello = rpcClient.create(Hello.class);
        //3.进行远程调用
        String respond = hello.sayHello("stupid"); 
