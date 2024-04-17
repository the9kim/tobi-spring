package spring.ch6_aop.d_java_reflection;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class HelloHandler implements InvocationHandler {

    // it makes it possible to apply for every type of targets
    Object target;

    public HelloHandler(Hello target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object ret = method.invoke(target, args);

        // Check if the method is necessary to apply for an additional functionality
        if (ret instanceof String) {
            return ((String) ret).toLowerCase();
        } else {
            return ret;
        }
    }
}
