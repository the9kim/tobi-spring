package spring.ch7_sql_separation.j_interface_inheritance;

import java.util.Map;

public class SqlAdminService {

    private UpdatableSqlRegistry sqlRegistry;

    public void sqlEventListener(Map<String, String> sqlEvent) {
        sqlRegistry.updateSql(sqlEvent);
    }

    public void setSqlRegistry(UpdatableSqlRegistry sqlRegistry) {
        this.sqlRegistry = sqlRegistry;
    }
}
