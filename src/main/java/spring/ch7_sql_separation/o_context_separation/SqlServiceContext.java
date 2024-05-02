package spring.ch7_sql_separation.o_context_separation;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import spring.ch7_sql_separation.c_sql_service.SqlService;
import spring.ch7_sql_separation.d_jaxb.Sqlmap;
import spring.ch7_sql_separation.f_interface_separation.SqlRegistry;
import spring.ch7_sql_separation.i_resource_abstraction.OxmSqlService2;
import spring.ch7_sql_separation.k_embedded_db.EmbeddedDbSqlRegistry;

import javax.sql.DataSource;

import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.HSQL;

@Configuration
public class SqlServiceContext {

    @Bean
    public SqlService sqlService() {
        OxmSqlService2 oxmSqlService2 = new OxmSqlService2();
        oxmSqlService2.setUnmarshaller(unmarshaller());
        oxmSqlService2.setSqlRegistry(sqlRegistry());
        oxmSqlService2.setSqlMap(new FileSystemResource("/Users/the9kim/tobi-spring/sqlmap.xml"));
        return oxmSqlService2;
    }

    @Bean
    public SqlRegistry sqlRegistry() {
        EmbeddedDbSqlRegistry sqlRegistry = new EmbeddedDbSqlRegistry();
        sqlRegistry.setDataSource(embeddedDatabase());
        return sqlRegistry;
    }

    @Bean
    public Unmarshaller unmarshaller() {
        Jaxb2Marshaller unmarshaller = new Jaxb2Marshaller();
        unmarshaller.setClassesToBeBound(new Class[]{Sqlmap.class});
        return unmarshaller;
    }

    @Bean
    @Qualifier("embeddedDatabase")
    public DataSource embeddedDatabase() {
        EmbeddedDatabaseBuilder embeddedDbBuilder = new EmbeddedDatabaseBuilder();
        return embeddedDbBuilder
                .setType(HSQL)
                .addScript("classpath:schema2.sql")
                .build();
    }
}
