package spring.ch3_template.j_refactoring;


import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import spring.ch1_object_and_dependency.User;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDao19 {

    private JdbcTemplate jdbcTemplate;

    // Register Row Mapper as an instance variable to remove duplication.
    private RowMapper<User> rowMapper = new RowMapper<User>() {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getString("id"));
            user.setName(rs.getString("name"));
            user.setPassword(rs.getString("password"));

            return user;
        }
    };

    public void add(final User user) throws SQLException {
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
                rowMapper
        );
    }

    public List<User> getAll() {
        return jdbcTemplate.query(
                "SELECT * FROM users",
                rowMapper
        );
    }

    public void deleteAll() throws SQLException {
        jdbcTemplate.update("DELETE FROM users");
    }

    public int getCount() throws SQLException {
        return jdbcTemplate.queryForObject("SELECT COUNT(users.id) AS count FROM users", Integer.class);
    }


    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
}
