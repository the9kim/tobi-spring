package spring.ch7_sql_separation.k_embedded_db;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import spring.ch7_sql_separation.j_interface_inheritance.UpdatableSqlRegistry;

import javax.sql.DataSource;
import java.util.Map;

public class EmbeddedDbSqlRegistry implements UpdatableSqlRegistry {

    private JdbcTemplate template;

    @Override
    public String findSql(String key) throws SqlNotFoundException {
        try {
            return template.queryForObject("SELECT sql_ FROM sqlmap WHERE key_ = ?", new Object[]{key}, String.class);
        } catch (EmptyResultDataAccessException e) {
            throw new SqlNotFoundException(key + "에 해당하는 SQL이 존재하지 않습니다.");
        }
    }

    @Override
    public void registerSql(String key, String sql) {
        template.update("INSERT INTO sqlmap(key_, sql_) VALUES(?,?)", key, sql);
    }

    @Override
    public void updateSql(String key, String sql) {
        int updated = template.update("UPDATE sqlmap SET sql_ = ? WHERE key_ = ?", sql, key);
        if (updated == 0) {
            throw new SqlUpdateFailureException(key + "에 해당하는 SQL이 존재하지 않습니다.");
        }
    }

    @Override
    public void updateSql(Map<String, String> sqlmap) {
        for (Map.Entry<String, String> entry : sqlmap.entrySet()) {
            updateSql(entry.getKey(), entry.getValue());
        }
    }

    public void setDataSource(DataSource dataSource) {
        template = new JdbcTemplate(dataSource);
    }

}
