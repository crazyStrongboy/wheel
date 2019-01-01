package github.com.crazyStrongboy;

import github.com.crazyStrongboy.annotation.RpcAnnotation;

/**
 * @author mars_jun
 * @version 2019/1/1 12:30
 */

@RpcAnnotation(Hello.class)
public class HelloImpl implements Hello {

    public String sayHello(String msg) {
        return "hello , " + msg;
    }
}
