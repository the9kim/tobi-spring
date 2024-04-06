package spring.ground.template_callback;

import org.springframework.dao.EmptyResultDataAccessException;
import spring.ch1_object_and_dependency.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Dao {

    private JdbcContext jdbcContext;

    public void add(User user) throws SQLException {
        jdbcContext.workWithPreparedStatement(new StatementStrategy() {
            @Override
            public PreparedStatement makePreparedStatement(Connection conn) throws SQLException {
                PreparedStatement ps = conn.prepareStatement("INSERT INTO users(id, name, password) VALUES(?,?,?)");
                ps.setString(1, user.getId());
                ps.setString(2, user.getName());
                ps.setString(3, user.getPassword());
                return ps;
            }
        });
    }

    public void deleteAll() throws SQLException {
        jdbcContext.workWithPreparedStatement(new StatementStrategy() {
            @Override
            public PreparedStatement makePreparedStatement(Connection conn) throws SQLException {
                return conn.prepareStatement("DELETE FROM users");
            }
        });
    }

    public User get(String id) throws SQLException {
        return jdbcContext.workWithPreparedStatement(
                new StatementStrategy() {
                    @Override
                    public PreparedStatement makePreparedStatement(Connection conn) throws SQLException {
                        PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE id = ?");
                        ps.setString(1, id);
                        return ps;
                    }
                },
                new MapperStrategy<User>() {
                    @Override
                    public User mapRow(ResultSet rs) throws SQLException {
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
                });
    }

    public void setDataSource(DataSource dataSource) {
        this.jdbcContext = new JdbcContext();
        jdbcContext.setDataSource(dataSource);
    }
}
