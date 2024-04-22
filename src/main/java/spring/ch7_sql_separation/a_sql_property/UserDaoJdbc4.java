package spring.ch7_sql_separation.a_sql_property;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import spring.ch5_service_abstraction.Level;
import spring.ch5_service_abstraction.h_mail_service.User3;
import spring.ch5_service_abstraction.h_mail_service.UserDao3;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDaoJdbc4 implements UserDao3 {

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

    String sqlAdd;

    String sqlGet;

    String sqlGetAll;

    String sqlGetCount;

    String sqlUpdate;

    String sqlDeleteAll;

    @Override
    public void add(User3 user) {
        jdbcTemplate.update(
                this.sqlAdd,
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
                this.sqlGet,
                new Object[]{userId},
                rowMapper
        );
    }

    @Override
    public List<User3> getAll() {
        return jdbcTemplate.query(
                this.sqlGetAll,
                rowMapper);
    }

    @Override
    public int getCount() {
        return jdbcTemplate.queryForObject(
                this.sqlGetCount,
                Integer.class
        );
    }

    @Override
    public void update(User3 user) {
        jdbcTemplate.update(
                this.sqlUpdate,
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
        jdbcTemplate.update(this.sqlDeleteAll);
    }

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void setRowMapper(RowMapper<User3> rowMapper) {
        this.rowMapper = rowMapper;
    }

    public void setSqlAdd(String sqlAdd) {
        this.sqlAdd = sqlAdd;
    }

    public void setSqlGet(String sqlGet) {
        this.sqlGet = sqlGet;
    }

    public void setSqlGetAll(String sqlGetAll) {
        this.sqlGetAll = sqlGetAll;
    }

    public void setSqlGetCount(String sqlGetCount) {
        this.sqlGetCount = sqlGetCount;
    }

    public void setSqlUpdate(String sqlUpdate) {
        this.sqlUpdate = sqlUpdate;
    }

    public void setSqlDeleteAll(String sqlDeleteAll) {
        this.sqlDeleteAll = sqlDeleteAll;
    }
}
