package github.com.crazyStrongboy.jdk_api;

import github.com.crazyStrongboy.inter.Driver;

/**
 * @author mars_jun
 * @version 2019/2/26 10:02
 */
public class JdkDriver1 implements Driver {
    @Override
    public void connect(String name) {
        System.out.println("JdkDriver1 connect :"+name);
    }
}
