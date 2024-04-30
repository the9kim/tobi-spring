package spring.ch7_sql_separation.i_resource_abstraction;

import org.springframework.core.io.Resource;
import org.springframework.oxm.Unmarshaller;
import spring.ch7_sql_separation.c_sql_service.SqlService;
import spring.ch7_sql_separation.d_jaxb.SqlType;
import spring.ch7_sql_separation.d_jaxb.Sqlmap;
import spring.ch7_sql_separation.f_interface_separation.SqlReader;
import spring.ch7_sql_separation.f_interface_separation.SqlRegistry;
import spring.ch7_sql_separation.g_default_dependency.BaseSqlService;
import spring.ch7_sql_separation.g_default_dependency.HashMapSqlRegistry;

import javax.annotation.PostConstruct;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.IOException;

public class OxmSqlService2 implements SqlService {

    private final BaseSqlService baseSqlService = new BaseSqlService();
    private final OxmSqlReader oxmSqlReader = new OxmSqlReader();
    private SqlRegistry sqlRegistry = new HashMapSqlRegistry();

    @PostConstruct
    public void loadSql() {
        baseSqlService.setSqlReader(this.oxmSqlReader);
        baseSqlService.setSqlRegistry(this.sqlRegistry);
        baseSqlService.loadSql();
    }

    @Override
    public String getSql(String key) {
        return baseSqlService.getSql(key);
    }

    public void setSqlRegistry(SqlRegistry sqlRegistry) {
        this.sqlRegistry = sqlRegistry;
    }

    public void setUnmarshaller(Unmarshaller unmarshaller) {
        this.oxmSqlReader.setUnmarshaller(unmarshaller);
    }

    public void setSqlMap(Resource sqlMap) {
        this.oxmSqlReader.setSqlMap(sqlMap);
    }

    private class OxmSqlReader implements SqlReader {

        private Unmarshaller unmarshaller;
        private Resource sqlMap;

        @Override
        public void readSql(SqlRegistry registry) {
            try {
                Source xmlSource = new StreamSource(sqlMap.getInputStream());
                Sqlmap unmarshal = (Sqlmap) this.unmarshaller.unmarshal(xmlSource);
                for (SqlType sql : unmarshal.getSql()) {
                    registry.registerSql(sql.getKey(), sql.getValue());
                }
            } catch (IOException e) {
                throw new IllegalArgumentException(this.sqlMap.getFilename() + "을 가져올 수 없습니다.", e);
            }
        }

        public void setUnmarshaller(Unmarshaller unmarshaller) {
            this.unmarshaller = unmarshaller;
        }

        public void setSqlMap(Resource sqlMap) {
            this.sqlMap = sqlMap;
        }
    }
}
