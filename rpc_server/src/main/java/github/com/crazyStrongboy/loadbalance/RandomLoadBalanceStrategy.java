package github.com.crazyStrongboy.loadbalance;

import java.util.List;
import java.util.Random;

/**
 * @author mars_jun
 * @version 2019/1/1 13:05
 */
public class RandomLoadBalanceStrategy extends AbstractLoadBalanceStrategy {

    @Override
    public String doSelect(List<String> urls) {
        int length = urls.size();
        return urls.get(new Random().nextInt(length));
    }
}
