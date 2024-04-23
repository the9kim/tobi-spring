package spring.ch7_sql_separation.d_jaxb;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sqlmapType", propOrder = {"sql"})
@XmlRootElement(name = "sqlmap")
public class Sqlmap {
    @XmlElement(required = true)
    protected List<SqlType> sql;

    public List<SqlType> getSql() {
        if (sql == null) {
            sql = new ArrayList<SqlType>();
        }
        return this.sql;
    }

    public void setSql(List<SqlType> sql) {
        this.sql = sql;
    }
}
