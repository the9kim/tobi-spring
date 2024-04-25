package spring.ch7_sql_separation.h_oxm_service_abstraction;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import spring.ch7_sql_separation.d_jaxb.SqlType;
import spring.ch7_sql_separation.d_jaxb.Sqlmap;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/UserService28-Test-applicationContext.xml")
public class JaxbTest2 {


    @Autowired
    Unmarshaller unmarshaller;

    @Autowired
    Marshaller marshaller;

    public static void main(String[] args) {
        JUnitCore.main("spring.ch7_sql_separation.h_oxm_service_abstraction.JaxbTest2");
    }

    @Test
    public void readSqlmapMarshalTest() throws IOException {
        SqlType sql1 = new SqlType();
        sql1.setKey("add");
        sql1.setValue("insert");

        SqlType sql2 = new SqlType();
        sql2.setKey("get");
        sql2.setValue("select");

        Sqlmap sqlmap = new Sqlmap();
        sqlmap.setSql(new ArrayList<>(List.of(sql1, sql2)));

        Result result = new StreamResult(new File("./test-sqlmap.xml"));
        marshaller.marshal(sqlmap, result);
    }

    @Test
    public void readSqlmapUnmarshalTest() throws IOException {
        // I don't know why this doesn't work
//        Source xmlSource = new StreamSource(getClass().getResourceAsStream("sqlMap"));
        Source xmlSource = new StreamSource(new File("./test-sqlmap.xml"));
        Sqlmap sqlMap = (Sqlmap) this.unmarshaller.unmarshal(xmlSource);

        List<SqlType> sqlList = sqlMap.getSql();

        assertThat(sqlList.size(), is(2));
        assertThat(sqlList.get(0).getKey(), is("add"));
        assertThat(sqlList.get(0).getValue(), is("insert"));
        assertThat(sqlList.get(1).getKey(), is("get"));
        assertThat(sqlList.get(1).getValue(), is("select"));
    }
}
