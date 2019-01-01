package github.com.crazyStrongboy.client;

/**
 * @author mars_jun
 * @version 2019/1/1 15:45
 */
public interface RpcClientProxy {

    <T> T create(Class<T> clazz);
}
