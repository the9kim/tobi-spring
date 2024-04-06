package spring.ch3_template.i_jdbc_template;


import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import spring.ch1_object_and_dependency.User;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDao18 {

    private JdbcTemplate jdbcTemplate;

    public void add(final User user) throws SQLException {

        // Create a call-back by oneself
//        jdbcTemplate.update(
//                new PreparedStatementCreator() {
//                    @Override
//                    public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
//                        PreparedStatement ps = conn.prepareStatement("INSERT INTO users(id, name, password) VALUES(?, ?, ?)");
//                        ps.setString(1, user.getId());
//                        ps.setString(2, user.getName());
//                        ps.setString(3, user.getPassword());
//
//                        return ps;
//                    }
//                });

        jdbcTemplate.update(
                "INSERT INTO users(id, name, password) VALUES(?, ?, ?)",
                user.getId(),
                user.getName(),
                user.getPassword()
        );
    }

    public User get(String id) throws SQLException {

        return jdbcTemplate.queryForObject(
                "SELECT * FROM users WHERE id = ?",
                new Object[]{id},
                new RowMapper<User>() {
                    @Override
                    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                        User user = new User();
                        user.setId(rs.getString("id"));
                        user.setName(rs.getString("name"));
                        user.setPassword(rs.getString("password"));

                        return user;
                    }
                });
    }

    public List<User> getAll() {
        return jdbcTemplate.query(
                "SELECT * FROM users",
                new RowMapper<User>() {
                    @Override
                    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                        User user = new User();
                        user.setId(rs.getString("id"));
                        user.setName(rs.getString("name"));
                        user.setPassword(rs.getString("password"));

                        return user;
                    }
                });
    }

    public void deleteAll() throws SQLException {
        // Create a call-back by oneself
//        jdbcTemplate.update(new PreparedStatementCreator() {
//            @Override
//            public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
//                return conn.prepareStatement("DELETE FROM users");
//            }
//        });

        jdbcTemplate.update("DELETE FROM users");
    }

    public int getCount() throws SQLException {

        return jdbcTemplate.queryForObject("SELECT COUNT(users.id) AS count FROM users", Integer.class);
    }


    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
}
