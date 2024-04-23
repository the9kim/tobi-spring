package spring.ch7_sql_separation.e_bean_initialization;

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
import java.util.List;
import java.util.Map;

public class XmlSqlService2 implements SqlService {

    private Map<String, String> sqlMap = new HashMap<>();
    private String sqlmapFile;

    @PostConstruct
    public void loadSql() {
        try {
            JAXBContext context = JAXBContext.newInstance(Sqlmap.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            Sqlmap sqlmap = (Sqlmap) unmarshaller.unmarshal(new FileReader(sqlmapFile));
            List<SqlType> sqls = sqlmap.getSql();

            for (SqlType sql : sqls) {
                sqlMap.put(sql.getKey(), sql.getValue());
            }
        } catch (JAXBException | FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getSql(String key) {
        String sql = sqlMap.get(key);

        if (sql == null) {
            throw new SqlRetrievalFailureException(key + "를 이용해서 SQL을 찾을 수 없습니다.");
        } else {
            return sql;
        }
    }

    public void setSqlmapFile(String sqlmapFile) {
        this.sqlmapFile = sqlmapFile;
    }
}
