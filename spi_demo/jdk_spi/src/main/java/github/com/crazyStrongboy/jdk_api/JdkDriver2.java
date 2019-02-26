package github.com.crazyStrongboy.jdk_api;

import github.com.crazyStrongboy.inter.Driver;


/**
 * @author mars_jun
 * @version 2019/2/26 10:08
 */
public class JdkDriver2 implements Driver {
    @Override
    public void connect(String name) {
        System.out.println("JdkDriver2 connect :"+name);
    }
}
