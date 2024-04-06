package spring.ch1_object_and_dependency.c_inheritance;

import spring.ch1_object_and_dependency.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;

public class DUserDao extends UserDao3 {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        DUserDao dDao = new DUserDao();

        User user = new User();
        user.setId("1");
        user.setName("roy");
        user.setPassword("1234");

        dDao.add(user);

        System.out.println("user 등록 성공");

        User savedUser = dDao.get("1");
        System.out.println("유저 이름: " + savedUser.getName());
        System.out.println("유저 비밀 번호: " + savedUser.getPassword());
    }

    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/spring", "root", "kdw71007050^");
    }
}
