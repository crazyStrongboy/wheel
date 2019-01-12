package github.com.crazyStrongboy.loadbalance;

import java.util.List;

/**
 * @author mars_jun
 * @version 2019/1/12 10:48
 */
public abstract class AbstractLoadBalanceStrategy implements LoadBalance {

    @Override
    public String getAccessService(List<String> urls) {
        if (urls == null || urls.isEmpty()) {
            return null;
        }
        if (urls.size() == 1) {
            return urls.get(0);
        }
        return doSelect(urls);
    }

    public abstract String doSelect(List<String> urls);
}
