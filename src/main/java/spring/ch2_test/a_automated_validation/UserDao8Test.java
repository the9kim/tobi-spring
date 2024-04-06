package spring.ch2_test.a_automated_validation;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import spring.ch1_object_and_dependency.User;
import spring.ch1_object_and_dependency.h_xml.UserDao8;

import java.sql.SQLException;

public class UserDao8Test {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        ApplicationContext context = new GenericXmlApplicationContext("dao8-applicationContext.xml");
        UserDao8 userDao8 = context.getBean("userDao8", UserDao8.class);

        User user = new User("1", "roy", "1234");
        userDao8.add(user);

        User savedUser = userDao8.get(user.getId());

        if (!user.getName().equals(savedUser.getName())) {
            System.out.println("테스트 실패 (name)");
        } else if (!user.getPassword().equals(savedUser.getPassword())) {
            System.out.println("테스트 실패 (password)");
        } else {
            System.out.println("조회 테스트 성공");
        }
    }
}
