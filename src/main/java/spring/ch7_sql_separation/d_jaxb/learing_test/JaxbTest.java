package spring.ch7_sql_separation.d_jaxb.learing_test;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import org.junit.Test;
import spring.ch7_sql_separation.d_jaxb.SqlType;
import spring.ch7_sql_separation.d_jaxb.Sqlmap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class JaxbTest {

    @Test
    public void marshalTest() throws JAXBException {
        Book book = new Book();
        book.setId(1L);
        book.setName("book1");
        book.setAuthor("author1");
        book.setDate(new Date());

        JAXBContext context = JAXBContext.newInstance(Book.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.marshal(book, new File("./book.xml"));
    }

    @Test
    public void unmarshal() throws JAXBException, FileNotFoundException {
        JAXBContext context = JAXBContext.newInstance(Book.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        Book book = (Book) unmarshaller.unmarshal(new FileReader("./book.xml"));

        assertThat(book.getId(), is(1L));
        assertThat(book.getName(), is("book1"));
        System.out.println(Sqlmap.class.getPackage().getName());
        System.out.println(getClass());
    }

    @Test
    public void readSqlmapMarshalTest() throws JAXBException {
        SqlType sql1 = new SqlType();
        sql1.setKey("add");
        sql1.setValue("insert");

        SqlType sql2 = new SqlType();
        sql2.setKey("get");
        sql2.setValue("select");

        Sqlmap sqlmap = new Sqlmap();
        sqlmap.setSql(new ArrayList<>(List.of(sql1, sql2)));

        JAXBContext context = JAXBContext.newInstance(Sqlmap.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.marshal(sqlmap, new File("./test-sqlmap.xml"));
    }

    @Test
    public void readSqlmapUnmarshalTest() throws JAXBException, FileNotFoundException {
        JAXBContext context = JAXBContext.newInstance(Sqlmap.class);

        Unmarshaller unmarshaller = context.createUnmarshaller();

        Sqlmap sqlMap = (Sqlmap) unmarshaller.unmarshal(new FileReader("./test-sqlmap.xml"));

        List<SqlType> sqlList = sqlMap.getSql();

        assertThat(sqlList.size(), is(2));
        assertThat(sqlList.get(0).getKey(), is("add"));
        assertThat(sqlList.get(0).getValue(), is("insert"));
        assertThat(sqlList.get(1).getKey(), is("get"));
        assertThat(sqlList.get(1).getValue(), is("select"));
    }
}
