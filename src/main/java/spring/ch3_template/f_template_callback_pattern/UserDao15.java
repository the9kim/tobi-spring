package spring.ch3_template.f_template_callback_pattern;


import org.springframework.dao.EmptyResultDataAccessException;
import spring.ch1_object_and_dependency.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao15 {

    private JdbcContext15 jdbcContext15;

    public void add(final User user) throws SQLException {

        StatementStrategy15 strategy = new StatementStrategy15() {
            @Override
            public PreparedStatement makePreparedStatement(Connection conn) throws SQLException {
                String sql = "INSERT INTO users(id, name, password) VALUES(?, ?, ?)";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, user.getId());
                ps.setString(2, user.getName());
                ps.setString(3, user.getPassword());
                return ps;
            }
        };
        jdbcContext15.workWithStatementStrategy(strategy);
    }

    public User get(String id) throws SQLException {

        StatementStrategy15 psStrategy = new StatementStrategy15() {
            @Override
            public PreparedStatement makePreparedStatement(Connection conn) throws SQLException {
                String sql = "SELECT * FROM users WHERE id = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, id);
                return ps;
            }
        };

        ResultSetStrategy15 rsStrategy = new ResultSetStrategy15() {
            @Override
            public Object makeObject(ResultSet rs) throws SQLException {
                User user = null;

                if (rs.next()) {
                    user = new User();
                    user.setId(rs.getString("id"));
                    user.setName(rs.getString("name"));
                    user.setPassword(rs.getString("password"));
                }
                if (user == null) {
                    throw new EmptyResultDataAccessException(1);
                }
                return user;
            }
        };

        return (User) jdbcContext15.workWithStatementStrategy(psStrategy, rsStrategy);
    }

    public void deleteAll() throws SQLException {

        StatementStrategy15 strategy = new StatementStrategy15() {
            @Override
            public PreparedStatement makePreparedStatement(Connection conn) throws SQLException {
                String sql = "DELETE FROM users";
                return conn.prepareStatement(sql);
            }
        };

        jdbcContext15.workWithStatementStrategy(strategy);
    }

    public int getCount() throws SQLException {

        StatementStrategy15 psStrategy = new StatementStrategy15() {
            @Override
            public PreparedStatement makePreparedStatement(Connection conn) throws SQLException {
                String sql = "SELECT COUNT(users.id) AS count FROM users";
                return conn.prepareStatement(sql);
            }
        };

        ResultSetStrategy15 rsStrategy = new ResultSetStrategy15() {
            @Override
            public Object makeObject(ResultSet resultSet) throws SQLException {
                resultSet.next();
                return resultSet.getInt("count");
            }
        };

        return (int) jdbcContext15.workWithStatementStrategy(psStrategy, rsStrategy);
    }

    public void setDataSource(DataSource dataSource) {
        this.jdbcContext15 = new JdbcContext15();
        this.jdbcContext15.setDataSource(dataSource);
    }
}
