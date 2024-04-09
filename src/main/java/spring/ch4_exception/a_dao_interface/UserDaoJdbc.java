package spring.ch4_exception.a_dao_interface;

import spring.ch1_object_and_dependency.User;

import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import java.sql.ResultSet;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class UserDaoJdbc implements UserDao {

    JdbcTemplate jdbcTemplate;

    RowMapper<User> rowMapper = new RowMapper<User>() {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getString("id"));
            user.setName(rs.getString("name"));
            user.setPassword(rs.getString("password"));
            return user;
        }
    };

    @Override
    public void add(User user) {
        jdbcTemplate.update(
                "INSERT INTO users(id, name, password) VALUES(?,?,?)",
                user.getId(),
                user.getName(),
                user.getPassword());
    }

    @Override
    public User get(String userId) {
        return jdbcTemplate.queryForObject(
                "SELECT * FROM users WHERE id = ?",
                new Object[]{userId},
                rowMapper
        );
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query(
                "SELECT * FROM users",
                rowMapper);
    }

    @Override
    public int getCount() {
        return jdbcTemplate.queryForObject(
                "SELECT COUNT(id) AS count FROM users ORDER BY id ASC",
                Integer.class
        );
    }

    @Override
    public void deleteAll() {
        jdbcTemplate.update("DELETE FROM users");
    }

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
}
