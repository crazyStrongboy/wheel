package github.com.crazyStrongboy.service.impl;

import github.com.crazyStrongboy.annotation.SService;
import github.com.crazyStrongboy.service.HelloService;

@SService("service")
public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello(String name) {
        return "sayHello : " + name;
    }
}
