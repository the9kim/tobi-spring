package spring.ch7_sql_separation.k_embedded_db;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;

import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.HSQL;

public class EmbeddedDBBuilderTest {

    EmbeddedDatabase db;

    JdbcTemplate template;

    @Before
    public void setUp() {
        db = new EmbeddedDatabaseBuilder()
                .setType(HSQL)
                .addScript("classpath:schema2.sql")
                .addScript("classpath:data.sql")
                .build();

        template = new JdbcTemplate(db);
    }

    @After
    public void tearDown() {
        db.shutdown();
    }

    @Test
    public void initData() {
        assertThat(template.queryForObject("SELECT COUNT(*) FROM sqlmap", int.class), is(3));

        List<Map<String, Object>> list = template.queryForList("SELECT * FROM sqlmap ORDER BY key_ ASC");
        assertThat(list.get(0).get("key_"), is("KEY1"));
        assertThat(list.get(0).get("sql_"), is("SQL1"));
        assertThat(list.get(1).get("key_"), is("KEY2"));
        assertThat(list.get(1).get("sql_"), is("SQL2"));
        assertThat(list.get(2).get("key_"), is("KEY3"));
        assertThat(list.get(2).get("sql_"), is("SQL3"));
    }

    @Test
    public void insert() {
        template.update("INSERT INTO sqlmap(key_, sql_) VALUES(?, ?)", "KEY4", "SQL4");

        assertThat(template.queryForObject("SELECT COUNT(*) FROM sqlmap", int.class), is(4));
    }

}
