package es.startuphero.checkstyle.generator.beans;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author ozlem.ulag
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Module {

  @XmlAttribute
  private String name;

  @XmlElement(name = "property")
  private List<ModuleProperty> properties = new ArrayList<>();

  @XmlElement(name = "module")
  private List<Module> childs = new ArrayList<>();

  @XmlElement(name = "metadata")
  private Set<ModuleMetadata> metadatas = new LinkedHashSet<>();

  private Module parent;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<ModuleProperty> getProperties() {
    return properties;
  }

  public void setProperties(List<ModuleProperty> properties) {
    this.properties = properties;
  }

  public List<Module> getChilds() {
    return childs;
  }

  public void setChilds(List<Module> childs) {
    this.childs = childs;
  }

  public Module getParent() {
    return parent;
  }

  public void setParent(Module parent) {
    this.parent = parent;
  }

  public Set<ModuleMetadata> getMetadatas() {
    return metadatas;
  }

  public void setMetadatas(Set<ModuleMetadata> metadatas) {
    this.metadatas = metadatas;
  }

  @Override
  public String toString() {
    return "Module{" +
           "name='" + name + '\'' +
           ", properties=" + properties +
           ", childs=" + childs +
           ", metadatas=" + metadatas +
           ", parent=" + parent +
           '}';
  }
}
