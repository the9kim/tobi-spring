package spring.ch7_sql_separation.g_default_dependency;

import spring.ch7_sql_separation.c_sql_service.SqlService;
import spring.ch7_sql_separation.f_interface_separation.SqlReader;
import spring.ch7_sql_separation.f_interface_separation.SqlRegistry;

import javax.annotation.PostConstruct;

import static spring.ch7_sql_separation.f_interface_separation.SqlRegistry.*;

public class BaseSqlService implements SqlService {

    protected SqlReader sqlReader;

    protected SqlRegistry sqlRegistry;

    @PostConstruct
    public void loadSql() {
        this.sqlReader.readSql(this.sqlRegistry);
    }

    @Override
    public String getSql(String key) {
        try {
            return sqlRegistry.findSql(key);
        } catch (SqlNotFoundException e) {
            throw new SqlRetrievalFailureException(e.getMessage(), e);
        }
    }

    public void setSqlReader(SqlReader sqlReader) {
        this.sqlReader = sqlReader;
    }

    public void setSqlRegistry(SqlRegistry sqlRegistry) {
        this.sqlRegistry = sqlRegistry;
    }
}
