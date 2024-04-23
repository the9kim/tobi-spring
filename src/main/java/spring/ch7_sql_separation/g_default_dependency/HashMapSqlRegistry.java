package spring.ch7_sql_separation.g_default_dependency;

import spring.ch7_sql_separation.f_interface_separation.SqlRegistry;

import java.util.HashMap;
import java.util.Map;

public class HashMapSqlRegistry implements SqlRegistry {

    private Map<String, String> sqlMap = new HashMap<>();

    @Override
    public void registerSql(String key, String sql) {
        this.sqlMap.put(key, sql);
    }

    @Override
    public String findSql(String key) {
        String sql = this.sqlMap.get(key);
        if (sql == null) {
            throw new SqlNotFoundException(key + "를 이용하는 SQL이 존재하지 않습니다.");
        }
        return sql;
    }
}
