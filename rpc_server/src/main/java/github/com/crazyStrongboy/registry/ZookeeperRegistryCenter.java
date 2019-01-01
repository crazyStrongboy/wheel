package github.com.crazyStrongboy.registry;

import github.com.crazyStrongboy.config.ZookeeperConfig;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

/**
 * @author mars_jun
 * @version 2019/1/1 12:34
 */
public class ZookeeperRegistryCenter implements RegistryCenter {

    private CuratorFramework curator;


    {
        curator = CuratorFrameworkFactory.builder().connectString(ZookeeperConfig.CONNECT_ADDRESS)
                .sessionTimeoutMs(1000)    // 连接超时时间
                .connectionTimeoutMs(1000) // 会话超时时间
                // 刚开始重试间隔为1秒，之后重试间隔逐渐增加，最多重试不超过三次
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .build();
        curator.start();
    }

    public void register(String serviceName, String serviceAddress) {
        try {
            String servicePath = ZookeeperConfig.ZOOKEEPER_PATH + "/" + serviceName;
            if (curator.checkExists().forPath(servicePath) == null) {
                curator.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(servicePath, "0".getBytes());
            }
            String addressPath = servicePath + "/" + serviceAddress;
            String nodePath = curator.create().withMode(CreateMode.EPHEMERAL).forPath(addressPath, "0".getBytes());
            System.out.println("register success :" + nodePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
