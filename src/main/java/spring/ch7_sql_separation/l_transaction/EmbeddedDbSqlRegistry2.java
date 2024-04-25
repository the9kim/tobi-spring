package spring.ch7_sql_separation.l_transaction;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import spring.ch7_sql_separation.j_interface_inheritance.UpdatableSqlRegistry;

import javax.sql.DataSource;
import java.util.Map;

public class EmbeddedDbSqlRegistry2 implements UpdatableSqlRegistry {

    private JdbcTemplate jdbcTemplate;
    private TransactionTemplate transactionTemplate;

    @Override
    public String findSql(String key) throws SqlNotFoundException {
        try {
            return jdbcTemplate.queryForObject("SELECT sql_ FROM sqlmap WHERE key_ = ?", new Object[]{key}, String.class);
        } catch (EmptyResultDataAccessException e) {
            throw new SqlNotFoundException(key + "에 해당하는 SQL이 존재하지 않습니다.");
        }
    }

    @Override
    public void registerSql(String key, String sql) {
        jdbcTemplate.update("INSERT INTO sqlmap(key_, sql_) VALUES(?,?)", key, sql);
    }

    @Override
    public void updateSql(String key, String sql) {
        int updated = jdbcTemplate.update("UPDATE sqlmap SET sql_ = ? WHERE key_ = ?", sql, key);
        if (updated == 0) {
            throw new SqlUpdateFailureException(key + "에 해당하는 SQL이 존재하지 않습니다.");
        }
    }

    @Override
    public void updateSql(final Map<String, String> sqlmap) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                for (Map.Entry<String, String> entry : sqlmap.entrySet()) {
                    updateSql(entry.getKey(), entry.getValue());
                }
            }
        });
    }

    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        transactionTemplate = new TransactionTemplate(new DataSourceTransactionManager(dataSource));
    }
}
