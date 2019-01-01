This is a simple rpc framework rudiment!

    Server:
        //1.The first step is to have a registry
        RegistryCenter registry = new ZookeeperRegistryCenter();
        //2. The second step is to have a rpcServer
        RpcServer rpcServer = new RpcServer(registry,"127.0.0.1:8080");
        //3. The third step is to register service
        Hello hello = new HelloImpl();
        rpcServer.bind(hello);
        //4. The last step is to publish service
        rpcServer.publish();
    
    
    Client:
        //1.The first step is to have a discovery node
        ServiceDiscovery discovery = new ZookeeperServiceDiscovery();
        //2.The second step is to create a instance,this can connect server
        RpcClientProxy rpcClient = new JdkRpcClientProxy(discovery);
        Hello hello = rpcClient.create(Hello.class);
        //3. Rpc invoke
        String respond = hello.sayHello("stupid"); 
    
