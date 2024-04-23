package spring.ch7_sql_separation.d_jaxb;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import spring.ch7_sql_separation.c_sql_service.SqlService;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XmlSqlService implements SqlService {

    private Map<String, String> sqlMap = new HashMap<>();

    public XmlSqlService() {
        try {
            JAXBContext context = JAXBContext.newInstance(Sqlmap.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            Sqlmap sqlmap = (Sqlmap) unmarshaller.unmarshal(new FileReader("./sqlmap.xml"));
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
}
