package spring.ch1_object_and_dependency.g_application_context;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DaoFactory {

    @Bean
    public UserDao7 userDao7() {
        return new UserDao7(countingConnectionMaker());
    }

    @Bean
    public CountingConnectionMaker countingConnectionMaker() {
        return new CountingConnectionMaker(connectionMaker());
    }

    @Bean
    public DConnectionMaker connectionMaker() {
        return new DConnectionMaker();
    }
}
