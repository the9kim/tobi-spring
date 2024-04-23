package spring.ch7_sql_separation.f_interface_separation;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import spring.ch7_sql_separation.c_sql_service.SqlService;
import spring.ch7_sql_separation.d_jaxb.SqlType;
import spring.ch7_sql_separation.d_jaxb.Sqlmap;

import javax.annotation.PostConstruct;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class XmlSqlService3 implements SqlService, SqlReader, SqlRegistry {

    private SqlReader sqlReader;
    private String sqlMapFile;

    private SqlRegistry sqlRegistry;
    private Map<String, String> sqlMap = new HashMap<>();

    @PostConstruct
    public void loadSql() {
        sqlReader.readSql(sqlRegistry);
    }

    @Override
    public void readSql(SqlRegistry registry) {
        try {
            JAXBContext context = JAXBContext.newInstance(Sqlmap.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            Sqlmap sqlmap = (Sqlmap) unmarshaller.unmarshal(new FileReader(this.sqlMapFile));

            for (SqlType sql : sqlmap.getSql()) {
                registry.registerSql(sql.getKey(), sql.getValue());
            }
        } catch (JAXBException | FileNotFoundException e) {
            new RuntimeException(e);
        }
    }

    @Override
    public String getSql(String key) {
        try {
            String sql = sqlRegistry.findSql(key);
            return sql;
        } catch (SqlNotFoundException e) {
            throw new SqlRetrievalFailureException(e.getMessage(), e);
        }
    }

    @Override
    public String findSql(String key) {
        String sql = this.sqlMap.get(key);
        if (sql == null) {
            throw new SqlNotFoundException(key + "를 이용해서 SQL을 찾을 수 없습니다.");
        } else {
            return sql;
        }
    }

    @Override
    public void registerSql(String key, String sql) {
        this.sqlMap.put(key, sql);
    }

    public void setSqlReader(SqlReader sqlReader) {
        this.sqlReader = sqlReader;
    }

    public void setSqlRegistry(SqlRegistry sqlRegistry) {
        this.sqlRegistry = sqlRegistry;
    }

    public void setSqlMapFile(String sqlMapFile) {
        this.sqlMapFile = sqlMapFile;
    }
}
