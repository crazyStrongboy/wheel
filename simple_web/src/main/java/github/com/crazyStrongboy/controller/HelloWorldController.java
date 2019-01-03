package github.com.crazyStrongboy.controller;

import github.com.crazyStrongboy.annotation.SAutowired;
import github.com.crazyStrongboy.annotation.SController;
import github.com.crazyStrongboy.annotation.SRequestMapping;
import github.com.crazyStrongboy.annotation.SRequestParam;
import github.com.crazyStrongboy.service.ByeService;
import github.com.crazyStrongboy.service.HelloService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SController("helloWorld")
@SRequestMapping("/hello")
public class HelloWorldController {

    @SAutowired
    private HelloService helloService;

    @SAutowired
    private ByeService byeService;

    @SRequestMapping("/say")
    public String say(@SRequestParam("name") String name, @SRequestParam("value") String value, HttpServletRequest request, HttpServletResponse response) {
        return "say:" + name + ":" + value;
    }
}
