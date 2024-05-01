package spring.ch7_sql_separation.n_auto_wiring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import spring.ch5_service_abstraction.Level;
import spring.ch5_service_abstraction.h_mail_service.User3;
import spring.ch5_service_abstraction.h_mail_service.UserDao3;
import spring.ch7_sql_separation.c_sql_service.SqlService;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository("userDao3")
public class UserDaoJdbc7 implements UserDao3 {

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

    @Autowired
    SqlService sqlService;

    @Override
    public void add(User3 user) {
        jdbcTemplate.update(
                this.sqlService.getSql("userAdd"),
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
                this.sqlService.getSql("userGet"),
                new Object[]{userId},
                rowMapper
        );
    }

    @Override
    public List<User3> getAll() {
        return jdbcTemplate.query(
                this.sqlService.getSql("userGetAll"),
                rowMapper);
    }

    @Override
    public int getCount() {
        return jdbcTemplate.queryForObject(
                this.sqlService.getSql("userGetCount"),
                Integer.class
        );
    }

    @Override
    public void update(User3 user) {
        jdbcTemplate.update(
                this.sqlService.getSql("userUpdate"),
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
        jdbcTemplate.update(this.sqlService.getSql("userDeleteAll"));
    }

    @Autowired
    @Qualifier("dataSource")
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

//    public void setSqlService(SqlService sqlService) {
//        this.sqlService = sqlService;
//    }

}
