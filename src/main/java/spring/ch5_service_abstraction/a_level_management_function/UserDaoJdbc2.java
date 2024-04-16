package spring.ch5_service_abstraction.a_level_management_function;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import spring.ch5_service_abstraction.Level;
import spring.ch5_service_abstraction.User2;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDaoJdbc2 implements UserDao2 {

    JdbcTemplate jdbcTemplate;

    RowMapper<User2> rowMapper = new RowMapper<>() {
        @Override
        public User2 mapRow(ResultSet rs, int rowNum) throws SQLException {
            User2 user = new User2();
            user.setId(rs.getString("id"));
            user.setName(rs.getString("name"));
            user.setPassword(rs.getString("password"));
            user.setLevel(Level.valueOf(rs.getInt("level")));
            user.setLogin(rs.getInt("login"));
            user.setRecommend(rs.getInt("recommend"));
            return user;
        }
    };

    @Override
    public void add(User2 user) {
        jdbcTemplate.update(
                "INSERT INTO users2(id, name, password, level, login, recommend) VALUES(?,?,?,?,?,?)",
                user.getId(),
                user.getName(),
                user.getPassword(),
                user.getLevel().getValue(),
                user.getLogin(),
                user.getRecommend());
    }

    @Override
    public User2 get(String userId) {
        return jdbcTemplate.queryForObject(
                "SELECT * FROM users2 WHERE id = ?",
                new Object[]{userId},
                rowMapper
        );
    }

    @Override
    public List<User2> getAll() {
        return jdbcTemplate.query(
                "SELECT * FROM users2",
                rowMapper);
    }

    @Override
    public int getCount() {
        return jdbcTemplate.queryForObject(
                "SELECT COUNT(id) AS count FROM users2 ORDER BY id ASC",
                Integer.class
        );
    }

    @Override
    public void update(User2 user) {
        jdbcTemplate.update(
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

    @Override
    public void deleteAll() {
        jdbcTemplate.update("DELETE FROM users2");
    }

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
}
