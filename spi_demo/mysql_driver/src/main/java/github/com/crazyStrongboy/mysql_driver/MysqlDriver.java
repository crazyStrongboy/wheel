package github.com.crazyStrongboy.mysql_driver;

import github.com.crazyStrongboy.inter.Driver;

/**
 * @author mars_jun
 * @version 2019/2/26 9:57
 */
public class MysqlDriver implements Driver {
    @Override
    public void connect(String name) {
        System.out.println("初始化mysql connect :" + name);
    }
}
