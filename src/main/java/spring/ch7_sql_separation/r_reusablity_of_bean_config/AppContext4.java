package spring.ch7_sql_separation.r_reusablity_of_bean_config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import spring.ch5_service_abstraction.h_mail_service.DummyMailSender;
import spring.ch6_aop.l_transaction_attribute.UserService2;
import spring.ch7_sql_separation.q_property_source.SqlServiceContext3;
import spring.ch7_sql_separation.q_property_source.UserService36Test;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
//@Import({SqlServiceContext4.class})
@EnableSqlService
@ComponentScan(basePackages = "spring")
@PropertySource("/database.properties")
public class AppContext4 implements SqlMapConfig{

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

//    @Bean
//    public SqlMapConfig sqlMapConfig() {
//        return new UserSqlMapConfig();
//    }

    /**
     * Instead of Creating an implementation of the SqlMapConfig,
     * AppContext implements it by oneself and it is registered as a SqlMapConfig bean.
     * Therefore, SqlServiceContext can use it to load the sql map resource
     *
     */

    @Override
    public Resource getSqlMapResource() {
        return new FileSystemResource("/Users/the9kim/tobi-spring/sqlMap.xml");
    }

    @Configuration
    @Profile("context")
    public static class ProductionContext4 {

        @Bean
        public MailSender mailSender() {
            JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
            mailSender.setHost("mail.mycompany.com");
            return mailSender;
        }
    }

    @Configuration
    @Profile("test")
    public static class TestContext4 {
        @Bean
        public UserService2 testUserService16Impl() {
            return new UserService37Test.TestUserService16Impl();
        }

        @Bean
        public MailSender mailSender() {
            return new DummyMailSender();
        }
    }
}
