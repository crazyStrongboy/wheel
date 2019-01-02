package github.com.crazyStrongboy.controller;

import github.com.crazyStrongboy.annotation.SController;
import github.com.crazyStrongboy.annotation.SRequestMapping;
import github.com.crazyStrongboy.annotation.SRequestParam;

@SController("helloWorld")
@SRequestMapping("/hello")
public class HelloWorldController {

    @SRequestMapping("/say")
    public String say(@SRequestParam("name") String name, @SRequestParam("value") String value) {
        return "say:" + name + ":" + value;
    }
}
