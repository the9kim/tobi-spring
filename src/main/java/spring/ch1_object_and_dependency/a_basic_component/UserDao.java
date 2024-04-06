package spring.ch1_object_and_dependency.a_basic_component;

import spring.ch1_object_and_dependency.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        UserDao userDao = new UserDao();

        User user = new User();
        user.setId("1");
        user.setName("roy");
        user.setPassword("1234");

        userDao.add(user);

        System.out.println("user 등록 완료");


        User savedUser = userDao.get("1");
        System.out.println("name = " + savedUser.name);
        System.out.println("password = " + savedUser.password);
    }

    public void add(User user) throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/spring", "root", "kdw71007050^");
        String sql = "INSERT INTO users(id, name, password) VALUES(?,?,?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, user.getId());
        ps.setString(2, user.getName());
        ps.setString(3, user.getPassword());

        ps.executeUpdate();

        ps.close();

        conn.close();
    }

    public User get(String id) throws SQLException, ClassNotFoundException {

        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/spring", "root", "kdw71007050^");

        String sql = "SELECT * FROM users WHERE id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, id);

        ResultSet rs = ps.executeQuery();
        rs.next();

        User user = new User();

        user.setId(rs.getString("id"));
        user.setName(rs.getString("name"));
        user.setPassword(rs.getString("password"));

        return user;
    }

}
