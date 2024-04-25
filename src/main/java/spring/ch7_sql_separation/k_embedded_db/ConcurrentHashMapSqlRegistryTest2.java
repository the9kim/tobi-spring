package spring.ch7_sql_separation.k_embedded_db;

import org.junit.Before;
import org.junit.Test;
import spring.ch7_sql_separation.f_interface_separation.SqlRegistry.SqlNotFoundException;
import spring.ch7_sql_separation.j_interface_inheritance.ConcurrentHashMapSqlRegistry;
import spring.ch7_sql_separation.j_interface_inheritance.UpdatableSqlRegistry;
import spring.ch7_sql_separation.j_interface_inheritance.UpdatableSqlRegistry.SqlUpdateFailureException;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ConcurrentHashMapSqlRegistryTest2 extends AbstractUpdatableSqlRegistryTest {

    @Override
    protected UpdatableSqlRegistry setSqlRegistry() {
        return new ConcurrentHashMapSqlRegistry();
    }
}
