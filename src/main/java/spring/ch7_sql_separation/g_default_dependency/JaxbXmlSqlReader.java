package spring.ch7_sql_separation.g_default_dependency;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import spring.ch7_sql_separation.d_jaxb.SqlType;
import spring.ch7_sql_separation.d_jaxb.Sqlmap;
import spring.ch7_sql_separation.f_interface_separation.SqlReader;
import spring.ch7_sql_separation.f_interface_separation.SqlRegistry;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class JaxbXmlSqlReader implements SqlReader {

    private final static String DEFAULT_SQL_MAP_FILE = "./sqlmap.xml";
    private String sqlMapFile = DEFAULT_SQL_MAP_FILE;

    @Override
    public void readSql(SqlRegistry registry) {
        try {
            JAXBContext context = JAXBContext.newInstance(Sqlmap.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            Sqlmap sqlmap = (Sqlmap) unmarshaller.unmarshal(new FileReader(sqlMapFile));
            for (SqlType sql : sqlmap.getSql()) {
                registry.registerSql(sql.getKey(), sql.getValue());
            }
        } catch (JAXBException | FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void setSqlMapFile(String sqlMapFile) {
        this.sqlMapFile = sqlMapFile;
    }
}
