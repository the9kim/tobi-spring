package spring.ch1_object_and_dependency.d_interface;

import spring.ch1_object_and_dependency.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao4 {

    ConnectionMaker connectionMaker;

    public UserDao4() {
        this.connectionMaker = new DConnectionMaker();
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        UserDao4 userDao4 = new UserDao4();

        User user = new User();
        user.setId("1");
        user.setName("roy");
        user.setPassword("1234");

        userDao4.add(user);

        System.out.println("user 등록 성공");

        User savedUser = userDao4.get("1");
        System.out.println("유저 이름: " + savedUser.getName());
        System.out.println("유저 비밀 번호: " + savedUser.getPassword());
    }

    public void add(User user) throws SQLException {

        Connection conn = connectionMaker.makeConnection();
        String sql = "INSERT INTO users(id, name, password) VALUES(?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, user.getId());
        ps.setString(2, user.getName());
        ps.setString(3, user.getPassword());

        ps.executeUpdate();

        ps.close();

        conn.close();
    }

    public User get(String id) throws SQLException, ClassNotFoundException {

        Connection conn = connectionMaker.makeConnection();

        String sql = "SELECT * FROM users WHERE id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, id);

        ResultSet rs = ps.executeQuery();
        rs.next();

        User user = new User();

        user.setId(rs.getString("id"));
        user.setName(rs.getString("name"));
        user.setPassword(rs.getString("password"));

        rs.close();
        ps.close();
        conn.close();

        return user;
    }
}
