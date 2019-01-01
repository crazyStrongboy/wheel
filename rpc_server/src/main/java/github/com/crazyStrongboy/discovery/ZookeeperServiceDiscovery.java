package github.com.crazyStrongboy.discovery;

import github.com.crazyStrongboy.config.ZookeeperConfig;
import github.com.crazyStrongboy.loadbalance.LoadBalance;
import github.com.crazyStrongboy.loadbalance.RandomLoadBalance;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.List;

/**
 * @author mars_jun
 * @version 2019/1/1 13:11
 */
public class ZookeeperServiceDiscovery implements ServiceDiscovery {

    private List<String> handlers;

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

    public String discovery(String serviceName) {

        String servicePath = ZookeeperConfig.ZOOKEEPER_PATH + "/" + serviceName;
        try {
            handlers = curator.getChildren().forPath(servicePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //register watch
        registerWatch(servicePath);

        LoadBalance loadBalance = new RandomLoadBalance();
        return loadBalance.getAccessService(handlers);
    }

    private void registerWatch(final String servicePath) {
        PathChildrenCache pathChildrenCache = new PathChildrenCache(curator, servicePath, true);
        PathChildrenCacheListener pathChildrenCacheListener = new PathChildrenCacheListener() {
            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
                handlers = curator.getChildren().forPath(servicePath);
            }
        };
        pathChildrenCache.getListenable().addListener(pathChildrenCacheListener);
        try {
            pathChildrenCache.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
