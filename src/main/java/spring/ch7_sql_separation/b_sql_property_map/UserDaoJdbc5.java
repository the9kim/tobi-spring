package spring.ch7_sql_separation.b_sql_property_map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import spring.ch5_service_abstraction.Level;
import spring.ch5_service_abstraction.h_mail_service.User3;
import spring.ch5_service_abstraction.h_mail_service.UserDao3;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class UserDaoJdbc5 implements UserDao3 {

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

    Map<String, String> sqlMap;

    @Override
    public void add(User3 user) {
        jdbcTemplate.update(
                this.sqlMap.get("add"),
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
                this.sqlMap.get("get"),
                new Object[]{userId},
                rowMapper
        );
    }

    @Override
    public List<User3> getAll() {
        return jdbcTemplate.query(
                this.sqlMap.get("getAll"),
                rowMapper);
    }

    @Override
    public int getCount() {
        return jdbcTemplate.queryForObject(
                this.sqlMap.get("getCount"),
                Integer.class
        );
    }

    @Override
    public void update(User3 user) {
        jdbcTemplate.update(
                this.sqlMap.get("update"),
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
        jdbcTemplate.update(this.sqlMap.get("deleteAll"));
    }

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void setSqlMap(Map<String, String> sqlMap) {
        this.sqlMap = sqlMap;
    }
}
