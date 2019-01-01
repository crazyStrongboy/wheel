package github.com.crazyStrongboy.bean;

import java.io.Serializable;

/**
 * @author mars_jun
 * @version 2019/1/1 15:03
 */
public class RpcRequest implements Serializable {
    private String className;

    private Object[] params;

    private Class<?>[] types;

    private String methodName;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }

    public Class<?>[] getTypes() {
        return types;
    }

    public void setTypes(Class<?>[] types) {
        this.types = types;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
}
