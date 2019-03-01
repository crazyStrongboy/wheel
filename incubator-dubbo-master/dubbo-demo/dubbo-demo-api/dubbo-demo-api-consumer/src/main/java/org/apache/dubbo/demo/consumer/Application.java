/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.dubbo.demo.consumer;

import org.apache.dubbo.config.*;
import org.apache.dubbo.demo.DemoService;
import org.apache.dubbo.remoting.transport.mina.MinaTransporter;

import java.io.IOException;

public class Application {
    /**
     * In order to make sure multicast registry works, need to specify '-Djava.net.preferIPv4Stack=true' before
     * launch the application
     */
    public static void main(String[] args) throws IOException {
        System.setProperty("java.net.preferIPv4Stack", "true");
        ReferenceConfig<DemoService> reference = new ReferenceConfig<>();
        ApplicationConfig applicationConfig = new ApplicationConfig("dubbo-demo-api-consumer");
        MonitorConfig monitorConfig = new MonitorConfig();
        monitorConfig.setProtocol("registry");
        applicationConfig.setMonitor(monitorConfig);
        reference.setApplication(applicationConfig);
//        reference.setRegistry(new RegistryConfig("multicast://224.5.6.7:1234?unicast=false"));
        reference.setRegistry(new RegistryConfig("zookeeper://192.168.0.191:2181"));
//        reference.setRegistry(new RegistryConfig("zookeeper://134.175.35.208:2181"));
        ConsumerConfig consumerConfig = new ConsumerConfig();
        reference.setConsumer(consumerConfig);
        reference.setInterface(DemoService.class);
        DemoService service = reference.get();
        for (int i = 0; i < 100; i++) {
            String message = service.sayHello("dubbo"+i);
            System.out.println(message);
        }
        System.in.read();

    }
}
