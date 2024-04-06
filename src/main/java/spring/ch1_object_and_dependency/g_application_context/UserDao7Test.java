package spring.ch1_object_and_dependency.g_application_context;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import spring.ch1_object_and_dependency.User;

import java.sql.SQLException;

public class UserDao7Test {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        UserDao7 userDao7 = context.getBean("userDao7", UserDao7.class);

        User user = new User("1", "roy", "1234");
        userDao7.add(user);
        System.out.println("user 등록 성공");

        User savedUser = userDao7.get("1");
        System.out.println("User Name: " + savedUser.getName());
        System.out.println("User Password: " + savedUser.getName());

        CountingConnectionMaker ccm = context.getBean("countingConnectionMaker", CountingConnectionMaker.class);
        System.out.println("Connections 횟수: " + ccm.getCounter());
    }
}
