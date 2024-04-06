package spring.ch1_object_and_dependency.f_object_factory;

import spring.ch1_object_and_dependency.User;

import java.sql.SQLException;

public class UserDao6Test {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        UserDao6 userDao6 = new DaoFactory().userDao6();

        User user = new User("1", "roy", "1234");

        userDao6.add(user);

        System.out.println("사용자 등록 성공");

        User savedUser = userDao6.get("1");

        System.out.println("사용자 이름 " + savedUser.getName());
        System.out.println("사용자 비밀번호 " + savedUser.getPassword());
    }
}
