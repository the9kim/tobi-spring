package spring.ch7_sql_separation.o_context_separation;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailSender;
import spring.ch5_service_abstraction.h_mail_service.DummyMailSender;
import spring.ch6_aop.l_transaction_attribute.UserService2;

@Configuration
public class TestAppContext {

    @Bean
    public UserService2 testUserService13Impl() {
        return new UserService34Test.TestUserService13Impl();
    }

    @Bean
    public MailSender mailSender() {
        return new DummyMailSender();
    }
}
