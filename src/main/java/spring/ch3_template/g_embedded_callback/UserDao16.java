package spring.ch3_template.g_embedded_callback;


import org.springframework.dao.EmptyResultDataAccessException;
import spring.ch1_object_and_dependency.User;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao16 {

    private JdbcContext16 jdbcContext;

    public void add(final User user) throws SQLException {
        jdbcContext.executeSql(
                "INSERT INTO users(id, name, password) VALUES(?, ?, ?)",
                user.getId(),
                user.getName(),
                user.getPassword()
        );
    }

    public User get(String id) throws SQLException {

        ResultSetStrategy16 rsStrategy = new ResultSetStrategy16() {
            @Override
            public User makeObject(ResultSet rs) throws SQLException {
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

        return (User) jdbcContext.executeSqlWithRowMapping("SELECT * FROM users WHERE id = ?", rsStrategy, id);
    }

    public void deleteAll() throws SQLException {
        jdbcContext.executeSql("DELETE FROM users");
    }

    public int getCount() throws SQLException {

        ResultSetStrategy16 rsStrategy = new ResultSetStrategy16() {
            @Override
            public Integer makeObject(ResultSet resultSet) throws SQLException {
                resultSet.next();
                return resultSet.getInt("count");
            }
        };

        return (int) jdbcContext.executeSqlWithRowMapping("SELECT COUNT(users.id) AS count FROM users", rsStrategy);
    }



    public void setDataSource(DataSource dataSource) {
        this.jdbcContext = new JdbcContext16();
        this.jdbcContext.setDataSource(dataSource);
    }
}
