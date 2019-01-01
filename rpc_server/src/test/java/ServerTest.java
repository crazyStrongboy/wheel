import github.com.crazyStrongboy.registry.RegistryCenter;
import github.com.crazyStrongboy.registry.ZookeeperRegistryCenter;
import github.com.crazyStrongboy.server.RpcServer;

/**
 * @author mars_jun
 * @version 2019/1/1 12:30
 * simpleRpcServer
 * 1.注册服务，需要用zookeeper作为中间件（必须要有监控机制，防止服务列表刷新后调用者感知不到）
 * 2.用netty作为socket的连接层
 */
public class ServerTest {
    public static void main(String[] args) {
        Hello hello = new HelloImpl();
        RegistryCenter registry = new ZookeeperRegistryCenter();
        RpcServer rpcServer = new RpcServer(registry,"127.0.0.1:8080");
        rpcServer.bind(hello);
        rpcServer.publish();
    }
}
