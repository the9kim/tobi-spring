package spring.ch7_sql_separation.d_jaxb.learing_test;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.xml.bind.annotation.XmlType;

import java.util.Date;

@XmlRootElement(name = "book")
@XmlType(propOrder = {"id", "name", "date"})
public class Book {

    private Long id;
    private String name;
    private String author;
    private Date date;

    @XmlAttribute
    public void setId(Long id) {
        this.id = id;
    }

    @XmlElement(name = "title")
    public void setName(String name) {
        this.name = name;
    }

    @XmlElement
    public void setDate(Date date) {
        this.date = date;
    }

    @XmlTransient
    public void setAuthor(String author) {
        this.author = author;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public Date getDate() {
        return date;
    }
}
