package com.mars_jun.client.controller;

import com.mars_jun.client.aspect.MyHystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @Author marsJun
 * @Date 2019/4/15.
 */
@RestController
public class HelloController {

    private Random random = new Random();

    //    @HystrixCommand(fallbackMethod = "errorMethod",
//            commandProperties = {
//                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "100")
//            }
//    )
    @MyHystrixCommand
    @RequestMapping("/hello")
    public String hello(@RequestParam("name") String message) throws InterruptedException {
        int time = random.nextInt(200);
        System.out.println("spend time : " + time + "ms");
        Thread.sleep(time);
        System.out.println("hhhhhhhhhhhhhhhhhhhhhhhhh");
        return "hello world:" + message;
    }

    public String errorMethod(String message) {
        return "error message";
    }
}
