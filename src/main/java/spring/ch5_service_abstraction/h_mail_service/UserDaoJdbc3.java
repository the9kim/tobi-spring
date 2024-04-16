package spring.ch5_service_abstraction.h_mail_service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import spring.ch5_service_abstraction.Level;
import spring.ch5_service_abstraction.User2;
import spring.ch5_service_abstraction.a_level_management_function.UserDao2;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDaoJdbc3 implements UserDao3 {

    JdbcTemplate jdbcTemplate;

    RowMapper<User3> rowMapper = new RowMapper<>() {
        @Override
        public User3 mapRow(ResultSet rs, int rowNum) throws SQLException {
            User3 user = new User3();
            user.setId(rs.getString("id"));
            user.setName(rs.getString("name"));
            user.setPassword(rs.getString("password"));
            user.setEmail(rs.getString("email"));
            user.setLevel(Level.valueOf(rs.getInt("level")));
            user.setLogin(rs.getInt("login"));
            user.setRecommend(rs.getInt("recommend"));
            return user;
        }
    };

    @Override
    public void add(User3 user) {
        jdbcTemplate.update(
                "INSERT INTO users3(id, name, password, email, level, login, recommend) VALUES(?,?,?,?,?,?,?)",
                user.getId(),
                user.getName(),
                user.getPassword(),
                user.getEmail(),
                user.getLevel().getValue(),
                user.getLogin(),
                user.getRecommend());
    }

    @Override
    public User3 get(String userId) {
        return jdbcTemplate.queryForObject(
                "SELECT * FROM users3 WHERE id = ?",
                new Object[]{userId},
                rowMapper
        );
    }

    @Override
    public List<User3> getAll() {
        return jdbcTemplate.query(
                "SELECT * FROM users3",
                rowMapper);
    }

    @Override
    public int getCount() {
        return jdbcTemplate.queryForObject(
                "SELECT COUNT(id) AS count FROM users3 ORDER BY id ASC",
                Integer.class
        );
    }

    @Override
    public void update(User3 user) {
        jdbcTemplate.update(
                "UPDATE users3 SET " +
                        "name = ?, " +
                        "password = ?, " +
                        "email = ?, " +
                        "level = ?, " +
                        "login = ?, " +
                        "recommend = ? " +
                        "Where id = ?",
                user.getName(),
                user.getPassword(),
                user.getEmail(),
                user.getLevel().getValue(),
                user.getLogin(),
                user.getRecommend(),
                user.getId()
        );
    }

    @Override
    public void deleteAll() {
        jdbcTemplate.update("DELETE FROM users3");
    }

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
}
