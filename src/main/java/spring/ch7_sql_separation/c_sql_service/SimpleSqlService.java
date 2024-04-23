package spring.ch7_sql_separation.c_sql_service;

import java.util.Map;
import java.util.HashMap;

public class SimpleSqlService implements SqlService{

    Map<String, String> sqlMap = new HashMap<>();

    @Override
    public String getSql(String key) {
        String sql = sqlMap.get(key);

        if (sql == null) {
            throw new SqlRetrievalFailureException(key + "에 대한 SQL을 찾을 수 없습니다.");
        } else {
            return sql;
        }
    }

    public void setSqlMap(Map<String, String> sqlMap) {
        this.sqlMap = sqlMap;
    }
}
