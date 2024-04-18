package spring.ch6_aop.i_proxy_auto_creation;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.Test;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import spring.ch6_aop.h_proxy_factory_bean.ProxyFactoryBeanTest;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class PointCutTest {

    @Test
    public void classNamePointcutAdvisor() {
        NameMatchMethodPointcut classMethodPointcut = new NameMatchMethodPointcut() {
            public ClassFilter getClassFilter() {
                return new ClassFilter() {
                    public boolean matches(Class<?> clazz) {
                        return clazz.getSimpleName().startsWith("HelloT");
                    }
                };
            }
        };
        classMethodPointcut.setMappedName("sayH*");

        checkAdviced(new HelloTarget(), classMethodPointcut, true);

        class HelloWorld extends HelloTarget {
        }
        checkAdviced(new HelloWorld(), classMethodPointcut, false);

        class HelloToby extends HelloTarget {}
        checkAdviced(new HelloToby(), classMethodPointcut, true);
    }

    private void checkAdviced(Object target, Pointcut pointcut, boolean adviced) {
        ProxyFactoryBean pfBean = new ProxyFactoryBean();
        pfBean.setTarget(target);
        pfBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, new UppercaseAdvice()));

        Hello proxy = (Hello) pfBean.getObject();

        if (adviced) {
            assertThat(proxy.sayHello("roy"), is("HELLO ROY"));
            assertThat(proxy.sayHi("roy"), is("HI ROY"));
            assertThat(proxy.sayThankYou("roy"), is("Thank you roy"));
        } else {
            assertThat(proxy.sayHello("roy"), is("Hello roy"));
            assertThat(proxy.sayHi("roy"), is("Hi roy"));
            assertThat(proxy.sayThankYou("roy"), is("Thank you roy"));
        }
    }

    static class UppercaseAdvice implements MethodInterceptor {

        public Object invoke(MethodInvocation invocation) throws Throwable {
            Object ret = invocation.proceed();

            if (ret instanceof String) {
                return ((String) ret).toUpperCase();
            }
            return ret;
        }
    }

    static interface Hello {
        String sayHello(String name);

        String sayHi(String name);

        String sayThankYou(String name);
    }

    static class HelloTarget implements Hello {
        @Override
        public String sayHello(String name) {
            return "Hello " + name;
        }

        @Override
        public String sayHi(String name) {
            return "Hi " + name;
        }

        @Override
        public String sayThankYou(String name) {
            return "Thank you " + name;
        }
    }
}
