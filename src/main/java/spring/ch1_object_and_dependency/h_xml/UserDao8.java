package spring.ch1_object_and_dependency.h_xml;


import spring.ch1_object_and_dependency.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao8 {
    private DataSource dataSource;

    public void add(User user) throws SQLException {

        Connection conn = dataSource.getConnection();
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

        Connection conn = dataSource.getConnection();

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


    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
