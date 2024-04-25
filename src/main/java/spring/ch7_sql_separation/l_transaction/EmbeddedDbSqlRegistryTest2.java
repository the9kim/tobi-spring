package spring.ch7_sql_separation.l_transaction;

import org.junit.After;
import org.junit.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import spring.ch7_sql_separation.j_interface_inheritance.UpdatableSqlRegistry;
import spring.ch7_sql_separation.j_interface_inheritance.UpdatableSqlRegistry.SqlUpdateFailureException;
import spring.ch7_sql_separation.k_embedded_db.AbstractUpdatableSqlRegistryTest;
import spring.ch7_sql_separation.k_embedded_db.EmbeddedDbSqlRegistry;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.fail;
import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.HSQL;

public class EmbeddedDbSqlRegistryTest2 extends AbstractUpdatableSqlRegistryTest {

    EmbeddedDatabase db;

    @After
    public void tearDown() {
        db.shutdown();
    }

    protected UpdatableSqlRegistry setSqlRegistry() {
        db = new EmbeddedDatabaseBuilder()
                .setType(HSQL)
                .addScript("classpath:schema2.sql")
                .build();
        EmbeddedDbSqlRegistry2 embeddedDbSqlRegistry = new EmbeddedDbSqlRegistry2();
        embeddedDbSqlRegistry.setDataSource(db);
        return embeddedDbSqlRegistry;
    }

    @Test
    public void transactionalUpdate() {
        Map<String, String> sqlmap = new HashMap<>();
        sqlmap.put("KEY1", "Modified1");
        sqlmap.put("SQL9999!@#$", "Modified9999");

        try {
            sqlRegistry.updateSql(sqlmap);
            fail();
        } catch (SqlUpdateFailureException e) {}
        super.checkFindResult("SQL1", "SQL2", "SQL3");
    }
}
