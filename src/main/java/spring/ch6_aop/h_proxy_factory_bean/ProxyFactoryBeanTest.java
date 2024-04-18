package spring.ch6_aop.h_proxy_factory_bean;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.Test;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ProxyFactoryBeanTest {

    @Test
    public void proxyFactoryBeanWithAdvice() {
        ProxyFactoryBean pfBean = new ProxyFactoryBean();
        pfBean.setTarget(new HelloTarget());
        pfBean.addAdvice(new UppercaseAdvice());

        Hello proxy = (Hello) pfBean.getObject();

        assertThat(proxy.sayHello("roy"), is("HELLO ROY"));
        assertThat(proxy.sayHi("roy"), is("HI ROY"));
        assertThat(proxy.sayThankYou("roy"), is("THANK YOU ROY"));
    }

    @Test
    public void proxyFactoryBeanWithAdvisor() {
        ProxyFactoryBean pfBean = new ProxyFactoryBean();
        pfBean.setTarget(new HelloTarget());

        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedName("sayH*");

        pfBean.addAdvisor(
                new DefaultPointcutAdvisor(
                        pointcut,
                        new UppercaseAdvice()
                )
        );

        Hello proxy = (Hello) pfBean.getObject();

        assertThat(proxy.sayHello("roy"), is("HELLO ROY"));
        assertThat(proxy.sayHi("roy"), is("HI ROY"));
        assertThat(proxy.sayThankYou("roy"), is("Thank you roy"));
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
