package spring.ch7_sql_separation.j_interface_inheritance;

import spring.ch7_sql_separation.f_interface_separation.SqlRegistry;

import java.util.Map;

public interface UpdatableSqlRegistry extends SqlRegistry {

    void updateSql(String key, String sql);

    void updateSql(Map<String, String> sqlmap);

    class SqlUpdateFailureException extends RuntimeException {
        public SqlUpdateFailureException(String message) {
            super(message);
        }
    }
}
