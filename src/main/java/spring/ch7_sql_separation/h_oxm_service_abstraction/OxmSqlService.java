package spring.ch7_sql_separation.h_oxm_service_abstraction;

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

public class OxmSqlService implements SqlService {

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

    public void setSqlMapFile(String sqlMapFile) {
        this.oxmSqlReader.setSqlMapFile(sqlMapFile);
    }

    private class OxmSqlReader implements SqlReader {

        private static final String DEFAULT_SQL_MAP_FILE = "./sqlmap.xml";

        private Unmarshaller unmarshaller;
        private String sqlMapFile = DEFAULT_SQL_MAP_FILE;

        @Override
        public void readSql(SqlRegistry registry) {
            try {
                Source xmlSource = new StreamSource(new File(sqlMapFile));
                Sqlmap unmarshal = (Sqlmap) this.unmarshaller.unmarshal(xmlSource);
                for (SqlType sql : unmarshal.getSql()) {
                    registry.registerSql(sql.getKey(), sql.getValue());
                }
            } catch (IOException e) {
                throw new IllegalArgumentException(this.sqlMapFile + "을 가져올 수 없습니다.", e);
            }
        }

        public void setUnmarshaller(Unmarshaller unmarshaller) {
            this.unmarshaller = unmarshaller;
        }

        public void setSqlMapFile(String sqlMapFile) {
            this.sqlMapFile = sqlMapFile;
        }
    }
}
