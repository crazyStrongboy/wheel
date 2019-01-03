package github.com.crazyStrongboy.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface SAutowired {

    String value() default "";
}
