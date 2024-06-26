package spring.ch7_sql_separation.k_embedded_db;

import org.junit.Before;
import org.junit.Test;
import spring.ch7_sql_separation.f_interface_separation.SqlRegistry.SqlNotFoundException;
import spring.ch7_sql_separation.j_interface_inheritance.UpdatableSqlRegistry;
import spring.ch7_sql_separation.j_interface_inheritance.UpdatableSqlRegistry.SqlUpdateFailureException;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public abstract class AbstractUpdatableSqlRegistryTest {

    protected UpdatableSqlRegistry sqlRegistry;

    @Before
    public void setUp() {
        sqlRegistry = setSqlRegistry();

        sqlRegistry.registerSql("KEY1", "SQL1");
        sqlRegistry.registerSql("KEY2", "SQL2");
        sqlRegistry.registerSql("KEY3", "SQL3");
    }

    abstract protected UpdatableSqlRegistry setSqlRegistry();

    @Test
    public void find() {
        checkFindResult("SQL1", "SQL2", "SQL3");
    }

    protected void checkFindResult(String expected1, String expected2, String expected3) {
        assertThat(sqlRegistry.findSql("KEY1"), is(expected1));
        assertThat(sqlRegistry.findSql("KEY2"), is(expected2));
        assertThat(sqlRegistry.findSql("KEY3"), is(expected3));
    }

    @Test(expected = SqlNotFoundException.class)
    public void unknownKey() {
        sqlRegistry.findSql("SQL9999!@#$");
    }

    @Test
    public void updateSingle() {
        sqlRegistry.updateSql("KEY2", "Modified2");
        checkFindResult("SQL1", "Modified2", "SQL3");
    }

    @Test
    public void updateMulti() {
        Map<String, String> sqlmap = new HashMap<>();
        sqlmap.put("KEY1", "Modified1");
        sqlmap.put("KEY3", "Modified3");

        sqlRegistry.updateSql(sqlmap);
        checkFindResult("Modified1", "SQL2", "Modified3");
    }

    @Test(expected = SqlUpdateFailureException.class)
    public void updateWithNonExistingKey() {
        sqlRegistry.updateSql("SQL9999!@#$", "Modified1");
    }
}
