package spring.ch7_sql_separation.q_property_source;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import spring.ch5_service_abstraction.h_mail_service.DummyMailSender;
import spring.ch6_aop.l_transaction_attribute.UserService2;
import spring.ch7_sql_separation.p_profile.UserService35Test;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@Import({SqlServiceContext3.class})
@ComponentScan(basePackages = "spring")
@PropertySource("/database.properties")
public class AppContext3 {

    /**
     * the property values from the resource which is registered by @PropertySource annotation
     * is saved on the Environment type object
     */

//    @Autowired
//    Environment env;

//    @Bean
//    @Qualifier("dataSource")
//    public DataSource dataSource() {
//        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
//
//        try {
//            dataSource.setDriverClass((Class<? extends java.sql.Driver>) Class.forName(env.getProperty("db.driverClass")));
//        } catch (ClassNotFoundException e) {
//            throw new RuntimeException();
//        }
//
//        dataSource.setUrl(env.getProperty("db.url"));
//        dataSource.setUsername(env.getProperty("db.username"));
//        dataSource.setPassword(env.getProperty("db.password"));
//
//        return dataSource;
//    }

    @Value("${db.driverClass}")
    Class<? extends java.sql.Driver> driverClass;

    @Value("${db.url}")
    String url;

    @Value("${db.username}")
    String username;

    @Value("${db.password}")
    String password;

    @Bean
    @Qualifier("dataSource")
    public DataSource dataSource() {

        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();

        dataSource.setDriverClass(this.driverClass);
        dataSource.setUrl(this.url);
        dataSource.setUsername(this.username);
        dataSource.setPassword(this.password);

        return dataSource;
    }

    /**
     * This is a bean post-processor to insert values from property source into @value fields.
     */
    @Bean
    public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(dataSource());
        return transactionManager;
    }

    @Configuration
    @Profile("context")
    public static class ProductionContext3 {

        @Bean
        public MailSender mailSender() {
            JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
            mailSender.setHost("mail.mycompany.com");
            return mailSender;
        }
    }

    @Configuration
    @Profile("test")
    public static class TestContext3 {
        @Bean
        public UserService2 testUserService15Impl() {
            return new UserService36Test.TestUserService15Impl();
        }

        @Bean
        public MailSender mailSender() {
            return new DummyMailSender();
        }
    }
}
