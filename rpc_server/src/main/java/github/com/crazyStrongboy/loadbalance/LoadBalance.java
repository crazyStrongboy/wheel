package github.com.crazyStrongboy.loadbalance;

import java.util.List;

/**
 * @author mars_jun
 * @version 2019/1/1 13:04
 */
public interface LoadBalance {
    String getAccessService(List<String> urls);
}
