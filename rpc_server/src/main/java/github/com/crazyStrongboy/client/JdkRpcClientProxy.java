package github.com.crazyStrongboy.client;

import github.com.crazyStrongboy.discovery.ServiceDiscovery;
import github.com.crazyStrongboy.proxy.JdkProxyHandler;

import java.lang.reflect.Proxy;

/**
 * @author mars_jun
 * @version 2019/1/1 15:03
 */
public class JdkRpcClientProxy implements RpcClientProxy {
    private ServiceDiscovery serviceDiscovery;

    public JdkRpcClientProxy(ServiceDiscovery serviceDiscovery) {
        this.serviceDiscovery = serviceDiscovery;
    }

    public <T> T create(Class<T> interfaceClass) {
        JdkProxyHandler jdkProxyHandler = new JdkProxyHandler(interfaceClass, serviceDiscovery);
        return (T)Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[]{interfaceClass}, jdkProxyHandler);
    }

}
