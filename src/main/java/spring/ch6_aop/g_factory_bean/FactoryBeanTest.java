package spring.ch6_aop.g_factory_bean;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.context.ApplicationContext;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/FactoryBean-Test-applicationContext.xml")
public class FactoryBeanTest {

    @Autowired
    ApplicationContext context;

    public static void main(String[] args) {
        JUnitCore.main("spring.ch6_aop.g_factory_bean.FactoryBeanTest");
    }

    @Test
    public void beanCreationUsingReflection() throws Throwable {
        String s = (String) Class.forName("java.lang.String").newInstance();
        System.out.println(s.getClass());
    }

    @Test
    public void getObjectFromFactoryBean() {
        assertThat(context.getBean("message", Message.class), is(Message.class));
    }

    @Test
    public void getFactoryBean() {
        assertThat(context.getBean("&message", MessageFactoryBean.class), is(MessageFactoryBean.class));
    }

    @Test
    public void getText() {
        Message message = context.getBean("message", Message.class);
        assertThat(message.getText(), is("Hello"));
    }
}
