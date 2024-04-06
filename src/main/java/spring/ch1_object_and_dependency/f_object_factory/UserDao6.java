package spring.ch1_object_and_dependency.f_object_factory;

import spring.ch1_object_and_dependency.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao6 {

    ConnectionMaker connectionMaker;

    public UserDao6(ConnectionMaker connectionMaker) {
        this.connectionMaker = connectionMaker;
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
