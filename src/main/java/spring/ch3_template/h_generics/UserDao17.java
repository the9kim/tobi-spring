package spring.ch3_template.h_generics;


import org.springframework.dao.EmptyResultDataAccessException;
import spring.ch1_object_and_dependency.User;
import spring.ch3_template.g_embedded_callback.JdbcContext16;
import spring.ch3_template.g_embedded_callback.ResultSetStrategy16;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao17 {

    private JdbcContext17 jdbcContext;

    public void add(final User user) throws SQLException {
        jdbcContext.executeSql(
                "INSERT INTO users(id, name, password) VALUES(?, ?, ?)",
                user.getId(),
                user.getName(),
                user.getPassword()
        );
    }

    public User get(String id) throws SQLException {

        ResultSetStrategy17<User> rsStrategy = new ResultSetStrategy17<>() {
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

        return jdbcContext.executeSqlWithRowMapping(
                "SELECT * FROM users WHERE id = ?",
                rsStrategy,
                new Object[]{id});
    }

    public void deleteAll() throws SQLException {
        jdbcContext.executeSql("DELETE FROM users");
    }

    public int getCount() throws SQLException {

        ResultSetStrategy17<Integer> rsStrategy = new ResultSetStrategy17<>() {
            @Override
            public Integer makeObject(ResultSet resultSet) throws SQLException {
                resultSet.next();
                return resultSet.getInt("count");
            }
        };

        return jdbcContext.executeSqlWithRowMapping(
                "SELECT COUNT(users.id) AS count FROM users",
                rsStrategy,
                new Object[]{});
    }



    public void setDataSource(DataSource dataSource) {
        this.jdbcContext = new JdbcContext17();
        this.jdbcContext.setDataSource(dataSource);
    }
}
