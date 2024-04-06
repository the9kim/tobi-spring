package spring.ch1_object_and_dependency.e_client;

import spring.ch1_object_and_dependency.User;
import spring.ch1_object_and_dependency.d_interface.DConnectionMaker;

import java.sql.SQLException;

public class UserDao5Test {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        UserDao5 userDao5 = new UserDao5(new DConnectionMaker());

        User user = new User("1", "roy", "1234");

        userDao5.add(user);

        System.out.println("사용자 등록 성공");

        User savedUser = userDao5.get("1");

        System.out.println("사용자 이름 " + savedUser.getName());
        System.out.println("사용자 비밀번호 " + savedUser.getPassword());
    }
}
