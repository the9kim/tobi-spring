package spring.ch6_aop.d_java_reflection;

import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ProxyTest {

    @Test
    public void classType() {
        String word = "spring";

        System.out.println(word.getClass());
        System.out.println(String.class);
    }

    @Test
    public void invokeMethod() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        String word = "spring";

        /**
         * length()
         */

        // Get method information from the class meta information
        Method length = String.class.getMethod("length");

        // Execute a method using invoke method of the method object
        assertThat((int)length.invoke(word), is(6));


        /**
         * charAt()
         */
        Method charAt =  String.class.getMethod("charAt", int.class);
        assertThat(charAt.invoke(word, 0), is('s'));
    }

    @Test
    public void dynamicProxy() {
        Hello target = new HelloTarget();

        HelloHandler handler = new HelloHandler(target);

        Hello proxy = (Hello) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[]{Hello.class},
                handler);

        assertThat(proxy.sayHello("roy"), is("hello roy"));
        assertThat(proxy.sayHi("roy"), is("hi roy"));
        assertThat(proxy.sayThankYou("roy"), is("thank you roy"));
    }

}
