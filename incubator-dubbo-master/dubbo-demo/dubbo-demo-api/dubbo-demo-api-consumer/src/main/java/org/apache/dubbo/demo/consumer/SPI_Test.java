package org.apache.dubbo.demo.consumer;

import java.util.ServiceLoader;

/**
 * @author mars_jun
 * @version 2019/2/24 0:21
 */
public class SPI_Test {
    public static void main(String[] args) {
        ServiceLoader<Demo> loader = ServiceLoader.load(Demo.class);
        loader.forEach(d -> {
            d.say();
        });
    }
}
