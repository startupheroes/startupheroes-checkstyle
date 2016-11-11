package es.startuphero.checstyle.generator.beans;

import java.util.LinkedHashSet;
import java.util.Set;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Rule {

   @XmlElement
   private String key;

   @XmlElement
   private String name;

   @XmlElement
   private String htmlDescription;

   @XmlElement
   private String internalKey;

   @XmlElement(name = "param")
   private Set<RuleParam> params = new LinkedHashSet<>();

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

   public String getHtmlDescription() {
      return htmlDescription;
   }

   public void setHtmlDescription(String htmlDescription) {
      this.htmlDescription = htmlDescription;
   }

   public String getInternalKey() {
      return internalKey;
   }

   public void setInternalKey(String internalKey) {
      this.internalKey = internalKey;
   }

   public Set<RuleParam> getParams() {
      return params;
   }

   public void setParams(Set<RuleParam> params) {
      this.params = params;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      Rule rule = (Rule) o;

      return key != null ? key.equals(rule.key) : rule.key == null;
   }

   @Override
   public int hashCode() {
      return key != null ? key.hashCode() : 0;
   }

   @Override
   public String toString() {
      return "Rule{" +
             "key='" + key + '\'' +
             ", name='" + name + '\'' +
             ", htmlDescription='" + htmlDescription + '\'' +
             ", internalKey='" + internalKey + '\'' +
             ", params=" + params +
             '}';
   }

}
