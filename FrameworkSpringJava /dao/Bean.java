package FrameworkSpringJava.dao;

import javax.xml.bind.annotation.*;


@XmlRootElement(name = "bean")
@XmlAccessorType(XmlAccessType.FIELD)
public class Bean {
    @XmlAttribute(name = "id")
    private String id;

    @XmlAttribute(name = "class")
    private String className;

    @XmlElement(name = "property")
    private Property property;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property= property;
    }
}
