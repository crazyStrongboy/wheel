package github.com.crazyStrongboy.annotation;

import java.lang.annotation.*;

/**
 * @author mars_jun
 * @version 2019/1/1 13:21
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RpcAnnotation {
    Class<?> value();
}
