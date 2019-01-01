package github.com.crazyStrongboy.loadbalance;

import java.util.List;
import java.util.Random;

/**
 * @author mars_jun
 * @version 2019/1/1 13:05
 */
public class RandomLoadBalance implements LoadBalance {
    public String getAccessService(List<String> urls) {
        if (urls == null || urls.isEmpty()) {
            return null;
        }
        int length = urls.size();
        return urls.get(new Random().nextInt(length));
    }
}
