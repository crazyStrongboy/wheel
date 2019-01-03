package github.com.crazyStrongboy.service.impl;

import github.com.crazyStrongboy.annotation.SService;
import github.com.crazyStrongboy.service.ByeService;
import github.com.crazyStrongboy.service.HelloService;

@SService("service")
public class HelloServiceImpl implements HelloService, ByeService {
    @Override
    public String sayHello(String name) {
        return "sayHello : " + name;
    }

    @Override
    public String sayBye(String name) {
        return "sayBye : " + name;
    }
}
