package github.com.crazyStrongboy.oracle_driver;

import github.com.crazyStrongboy.inter.Driver;

/**
 * @author mars_jun
 * @version 2019/2/26 9:55
 */
public class OracleDriver implements Driver {
    @Override
    public void connect(String name) {
        System.out.println("初始化oracle driver:" + name);
    }
}
