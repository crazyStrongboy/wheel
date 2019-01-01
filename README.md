this is a simple rpc framework

Server:
        //1.The first step is to have a registry<br/>
        RegistryCenter registry = new ZookeeperRegistryCenter();<br/>
        //2. The second step is to have a rpcServer<br/>
        RpcServer rpcServer = new RpcServer(registry,"127.0.0.1:8080");<br/>
        //3. The third step is to register service<br/>
        Hello hello = new HelloImpl();<br/>
        rpcServer.bind(hello);<br/>
        //4. The last step is to publish service<br/>
        rpcServer.publish();<br/>
    
    
Client:
        //1.The first step is to have a discovery node<br/>
        ServiceDiscovery discovery = new ZookeeperServiceDiscovery();<br/>
        //2.The second step is to create a instance,this can connect server<br/>
        RpcClientProxy rpcClient = new JdkRpcClientProxy(discovery);<br/>
        Hello hello = rpcClient.create(Hello.class);<br/>
        //3. Rpc invoke<br/>
        String respond = hello.sayHello("stupid"); <br/>
