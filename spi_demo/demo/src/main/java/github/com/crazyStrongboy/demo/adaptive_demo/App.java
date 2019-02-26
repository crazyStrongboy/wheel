package github.com.crazyStrongboy.demo.adaptive_demo;

import github.com.crazyStrongboy.inter.Driver;

/**
 * @author mars_jun
 * @version 2019/2/26 10:31
 */
public class App {



    public static void main(String[] args) {
        // 加载Driver的实现类进入到缓存中
        ExtensionLoad<Driver> extensionLoad = new ExtensionLoad<>();
        extensionLoad.loadDirectory(Driver.class);

//        print();
        String name = "";
        if (args.length == 1){
            name = args[0];
        }else {
            name = "mysql_driver";
        }

        Driver mysql_driver = extensionLoad.getExtension(name);
        mysql_driver.connect("root");
    }




}
