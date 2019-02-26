package github.com.crazyStrongboy.demo.jdk_demo;

import github.com.crazyStrongboy.inter.Driver;

import java.util.ServiceLoader;

/**
 * @author mars_jun
 * @version 2019/2/26 10:54
 */
public class App {
    public static void main(String[] args) {
        // 这里会主动去加载resources/META-INF/services下面的相关接口
        // 想去加载其中某一个需要遍历
        // 加载方式很简单：ClassLoader.getSystemResources(path)即可解析出全路径类名
        ServiceLoader<Driver> drivers = ServiceLoader.load(Driver.class);
        drivers.forEach(
                driver -> driver.connect("mysql")
        );
    }
}
