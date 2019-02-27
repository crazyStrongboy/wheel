/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.dubbo.rpc.proxy.javassist;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.bytecode.Proxy;
import org.apache.dubbo.common.bytecode.Wrapper;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.proxy.AbstractProxyFactory;
import org.apache.dubbo.rpc.proxy.AbstractProxyInvoker;
import org.apache.dubbo.rpc.proxy.InvokerInvocationHandler;

/**
 * JavaassistRpcProxyFactory
 */
public class JavassistProxyFactory extends AbstractProxyFactory {

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getProxy(Invoker<T> invoker, Class<?>[] interfaces) {
        /**
         * 这里动态生成Proxy0对象，调用方法触发InvokerInvocationHandler.invoke()方法
         *
         * 这里的invoker === MockClusterInvoker(RegistryDirectory,FailoverClusterInvoker)
         *
         * FailoverClusterInvoker中Invoker<T> invoker = select(loadbalance, invocation, copyInvokers, invoked);选择出合适的invoker
         *
         * InvokerDelegate(ProtocolFilterWrapper(ProtocolListenerWrapper(DubboInvoker(--->netty发送流程))))
         */
        return (T) Proxy.getProxy(interfaces).newInstance(new InvokerInvocationHandler(invoker));
    }

    @Override
    public <T> Invoker<T> getInvoker(T proxy, Class<T> type, URL url) {
        // TODO Wrapper cannot handle this scenario correctly: the classname contains '$'  wrapper == Wrapper1@...
        final Wrapper wrapper = Wrapper.getWrapper(proxy.getClass().getName().indexOf('$') < 0 ? proxy.getClass() : type);
        return new AbstractProxyInvoker<T>(proxy, type, url) {
            @Override
            protected Object doInvoke(T proxy, String methodName,
                                      Class<?>[] parameterTypes,
                                      Object[] arguments) throws Throwable {
                //  wrapper是一个包装类，实际上调用的还是proxy的methodName的Method
                // 实际编译后的代码如下所示：
                /**
                 * public Object invokeMethod(Object o, String n, Class[] p, Object[] v) throws java.lang.reflect.InvocationTargetException {
                 *
                 *         Object $1 = o;
                 *         String $2 = n;
                 *         Class[] $3 = p;
                 *         Object[] $4 = v;
                 *
                 *         org.apache.dubbo.demo.DemoService w;
                 *         try {
                 *             w = ((org.apache.dubbo.demo.DemoService) $1);
                 *         } catch (Throwable e) {
                 *             throw new IllegalArgumentException(e);
                 *         }
                 *         try {
                 *             if ("sayHello".equals($2) && $3.length == 1) {
                 *                 return w.sayHello((java.lang.String) $4[0]);
                 *             }
                 *         } catch (Throwable e) {
                 *             throw new java.lang.reflect.InvocationTargetException(e);
                 *         }
                 *         throw new org.apache.dubbo.common.bytecode.NoSuchMethodException("Not found method \"" + $2 + "\" in class org.apache.dubbo.demo.DemoService.");
                 *     }
                 */
                return wrapper.invokeMethod(proxy, methodName, parameterTypes, arguments);
            }
        };
    }

}
