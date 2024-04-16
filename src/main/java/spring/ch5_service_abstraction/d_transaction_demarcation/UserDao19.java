package spring.ch5_service_abstraction.d_transaction_demarcation;


import org.springframework.dao.EmptyResultDataAccessException;
import spring.ch5_service_abstraction.Level;
import spring.ch5_service_abstraction.User2;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDao19 {

    private JdbcContext18 jdbcContext;

    public UserDao19(JdbcContext18 jdbcContext) {
        this.jdbcContext = jdbcContext;
    }

    public void add(final User2 user, Connection conn) throws SQLException {
        jdbcContext.update(
                conn,
                "INSERT INTO users2(id, name, password, level, login, recommend) VALUES(?,?,?,?,?,?)",
                user.getId(),
                user.getName(),
                user.getPassword(),
                user.getLevel().getValue(),
                user.getLogin(),
                user.getRecommend()
        );
    }

    public void update(final User2 user, Connection conn) throws SQLException {
        jdbcContext.update(
                conn,
                "UPDATE users2 SET " +
                        "name = ?, " +
                        "password = ?, " +
                        "level = ?, " +
                        "login = ?, " +
                        "recommend = ? " +
                        "Where id = ?",
                user.getName(),
                user.getPassword(),
                user.getLevel().getValue(),
                user.getLogin(),
                user.getRecommend(),
                user.getId()
        );
    }

    public User2 get(String id, Connection conn) throws SQLException {

        ResultSetStrategy18<User2> rsStrategy = new ResultSetStrategy18<>() {
            @Override
            public User2 makeObject(ResultSet rs) throws SQLException {
                User2 user = null;

                if (rs.next()) {
                    user = new User2();
                    user.setId(rs.getString("id"));
                    user.setName(rs.getString("name"));
                    user.setPassword(rs.getString("password"));
                    user.setLevel(Level.valueOf(rs.getInt("level")));
                    user.setLogin(rs.getInt("login"));
                    user.setRecommend(rs.getInt("recommend"));
                }
                if (user == null) {
                    throw new EmptyResultDataAccessException(1);
                }
                return user;
            }
        };

        return jdbcContext.query(
                conn,
                "SELECT * FROM users2 WHERE id = ?",
                rsStrategy,
                new Object[]{id});
    }

    public List<User2> getAll(Connection conn) throws SQLException {

        ResultSetStrategy18<List<User2>> rsStrategy = new ResultSetStrategy18<>() {

            @Override
            public List<User2> makeObject(ResultSet rs) throws SQLException {
                List<User2> users = new ArrayList<>();

                while (rs.next()) {
                    User2 user = new User2();
                    user.setId(rs.getString("id"));
                    user.setName(rs.getString("name"));
                    user.setPassword(rs.getString("password"));
                    user.setLevel(Level.valueOf(rs.getInt("level")));
                    user.setLogin(rs.getInt("login"));
                    user.setRecommend(rs.getInt("recommend"));
                    users.add(user);
                }

                return users;
            }
        };

        return jdbcContext.query(
                conn,
                "SELECT * FROM users2",
                rsStrategy,
                new Object[]{});
    }

    public void deleteAll(Connection conn) throws SQLException {
        jdbcContext.update(conn,"DELETE FROM users2");
    }

    public int getCount(Connection conn) throws SQLException {

        ResultSetStrategy18<Integer> rsStrategy = new ResultSetStrategy18<>() {
            @Override
            public Integer makeObject(ResultSet resultSet) throws SQLException {
                resultSet.next();
                return resultSet.getInt("count");
            }
        };

        return jdbcContext.query(
                conn,
                "SELECT COUNT(id) AS count FROM users2 ORDER BY id ASC",
                rsStrategy,
                new Object[]{});
    }

    public void setJdbcContext18(JdbcContext18 jdbcContext) {
        this.jdbcContext = jdbcContext;
    }
}
