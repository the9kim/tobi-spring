package spring.ch7_sql_separation.j_interface_inheritance;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentHashMapSqlRegistry implements UpdatableSqlRegistry {

    private ConcurrentHashMap<String, String> concurrentHashMap = new ConcurrentHashMap<>();

    @Override
    public String findSql(String key) throws SqlNotFoundException {
        String sql = concurrentHashMap.get(key);

        if (sql == null) {
            throw new SqlNotFoundException(key + "에 해당하는 SQL을 찾을 수 없습니다.");
        } else {
            return sql;
        }
    }

    @Override
    public void registerSql(String key, String sql) {
        concurrentHashMap.put(key, sql);
    }

    @Override
    public void updateSql(String key, String sql) throws SqlUpdateFailureException {
        if (concurrentHashMap.get(key) == null) {
            throw new SqlUpdateFailureException(key + "에 해당하는 SQL을 찾을 수 없습니다.");
        } else {
            concurrentHashMap.put(key, sql);
        }
    }

    @Override
    public void updateSql(Map<String, String> sql) throws SqlUpdateFailureException {
        for (Map.Entry<String, String> entry : sql.entrySet()) {
            updateSql(entry.getKey(), entry.getValue());
        }
    }

}
