package spring.ch7_sql_separation.n_auto_wiring;

import com.mysql.jdbc.Driver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.mail.MailSender;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import spring.ch5_service_abstraction.h_mail_service.DummyMailSender;
import spring.ch5_service_abstraction.h_mail_service.UserDao3;
import spring.ch6_aop.l_transaction_attribute.UserService2;
import spring.ch6_aop.m_annotation_transaction.UserService11Impl;
import spring.ch7_sql_separation.c_sql_service.SqlService;
import spring.ch7_sql_separation.c_sql_service.UserDaoJdbc6;
import spring.ch7_sql_separation.d_jaxb.Sqlmap;
import spring.ch7_sql_separation.f_interface_separation.SqlRegistry;
import spring.ch7_sql_separation.i_resource_abstraction.OxmSqlService2;
import spring.ch7_sql_separation.k_embedded_db.EmbeddedDbSqlRegistry;
import spring.ch7_sql_separation.m_annotation.UserService32Test;

import javax.sql.DataSource;

import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.HSQL;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages="spring")
public class TestApplicationContext2 {

    @Autowired
    UserDao3 userDao3;

    @Bean
    @Qualifier("dataSource")
    public DataSource dataSource() {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setDriverClass(Driver.class);
        dataSource.setUrl("jdbc:mysql://localhost:3306/spring");
        dataSource.setUsername("root");
        dataSource.setPassword("kdw71007050^");

        return dataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(dataSource());
        return transactionManager;
    }

//    @Bean
//    public UserDao3 userDao3() {
//        UserDaoJdbc7 userDaoJdbc7 = new UserDaoJdbc7();
//        userDaoJdbc7.setDataSource(dataSource());
//        userDaoJdbc7.setSqlService(sqlService());
//        return userDaoJdbc7;
//    }

//    @Bean
//    public UserService2 userService() {
//        UserService12Impl userService12 = new UserService12Impl();
//        userService12.setUserDao3(this.userDao3);
//        userService12.setMailSender(mailSender());
//        return userService12;
//    }

    @Bean
    public UserService2 testUserService12Impl() {
        UserService33Test.TestUserService12Impl testUserService12 = new UserService33Test.TestUserService12Impl();
        testUserService12.setUserDao3(this.userDao3);
        testUserService12.setMailSender(mailSender());
        return testUserService12;
    }

    @Bean
    public MailSender mailSender() {
        return new DummyMailSender();
    }

    @Bean
    public SqlService sqlService() {
        OxmSqlService2 oxmSqlService2 = new OxmSqlService2();
        oxmSqlService2.setUnmarshaller(unmarshaller());
        oxmSqlService2.setSqlRegistry(sqlRegistry());
        oxmSqlService2.setSqlMap(new FileSystemResource("/Users/the9kim/tobi-spring/sqlmap.xml"));
        return oxmSqlService2;
    }

    @Bean
    public SqlRegistry sqlRegistry() {
        EmbeddedDbSqlRegistry sqlRegistry = new EmbeddedDbSqlRegistry();
        sqlRegistry.setDataSource(embeddedDatabase());
        return sqlRegistry;
    }

    @Bean
    public Unmarshaller unmarshaller() {
        Jaxb2Marshaller unmarshaller = new Jaxb2Marshaller();
        unmarshaller.setClassesToBeBound(new Class[]{Sqlmap.class});
        return unmarshaller;
    }

    @Bean
    @Qualifier("embeddedDatabase")
    public DataSource embeddedDatabase() {
        EmbeddedDatabaseBuilder embeddedDbBuilder = new EmbeddedDatabaseBuilder();
        return embeddedDbBuilder
                .setType(HSQL)
                .addScript("classpath:schema2.sql")
                .build();
    }
}
