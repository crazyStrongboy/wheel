package org.apache.dubbo.demo.consumer;

public class Test {
    public Object invokeMethod(Object o, String n, Class[] p, Object[] v) throws java.lang.reflect.InvocationTargetException {
        Object $1 = o;
        String $2 = n;
        Class[] $3 = p;
        Object[] $4 = v;
        org.apache.dubbo.demo.DemoService w;
        try {
            w = ((org.apache.dubbo.demo.DemoService) $1);
        } catch (Throwable e) {
            throw new IllegalArgumentException(e);
        }
        try {
            if ("sayHello".equals($2) && $3.length == 1) {
                return w.sayHello((java.lang.String) $4[0]);
            }
        } catch (Throwable e) {
            throw new java.lang.reflect.InvocationTargetException(e);
        }
        throw new org.apache.dubbo.common.bytecode.NoSuchMethodException("Not found method \"" + $2 + "\" in class org.apache.dubbo.demo.DemoService.");
    }
}
