import github.com.crazyStrongboy.client.JdkRpcClientProxy;
import github.com.crazyStrongboy.client.RpcClientProxy;
import github.com.crazyStrongboy.discovery.ServiceDiscovery;
import github.com.crazyStrongboy.discovery.ZookeeperServiceDiscovery;

/**
 * @author mars_jun
 * @version 2019/1/1 15:02
 */
public class ClientTest {
    public static void main(String[] args) throws InterruptedException {
        ServiceDiscovery discovery = new ZookeeperServiceDiscovery();
        RpcClientProxy rpcClient = new JdkRpcClientProxy(discovery);
        Hello hello = rpcClient.create(Hello.class);
        for (int i = 0; i < 20; i++) {
            String respond = hello.sayHello("stupid", 30);
            System.err.println(respond);
            Thread.sleep(4000);
        }
        // 测试不存在的服务
//        Driver driver = rpcClient.create(Driver.class);
//        String r = null;
//        try {
//            r = driver.driving("faster");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        System.err.println(r);
    }
}
