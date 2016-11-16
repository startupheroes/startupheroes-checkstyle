package es.startuphero.checstyle.generator.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author ozlem.ulag
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class RuleParam {

  @XmlAttribute
  private String key;

  @XmlAttribute
  private String name;

  @XmlElement
  private String defaultValue;

  @XmlElement
  private String description;

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDefaultValue() {
    return defaultValue;
  }

  public void setDefaultValue(String defaultValue) {
    this.defaultValue = defaultValue;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    RuleParam ruleParam = (RuleParam) o;

    return key != null ? key.equals(ruleParam.key) : ruleParam.key == null;
  }

  @Override
  public int hashCode() {
    return key != null ? key.hashCode() : 0;
  }

  @Override
  public String toString() {
    return "RuleParam{" +
        "key='" + key + '\'' +
        ", name='" + name + '\'' +
        ", defaultValue='" + defaultValue + '\'' +
        ", description='" + description + '\'' +
        '}';
  }
}
