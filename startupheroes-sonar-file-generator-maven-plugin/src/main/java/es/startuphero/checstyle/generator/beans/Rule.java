package es.startuphero.checstyle.generator.beans;

import java.util.ArrayList;
import java.util.List;
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
   private String description;

   @XmlElement
   private String configKey;

   @XmlElement
   private RuleCategory category;

   @XmlElement(name = "param")
   private List<RuleParam> params = new ArrayList<>();

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

   public String getDescription() {
      return description;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public String getConfigKey() {
      return configKey;
   }

   public void setConfigKey(String configKey) {
      this.configKey = configKey;
   }

   public RuleCategory getCategory() {
      return category;
   }

   public void setCategory(RuleCategory category) {
      this.category = category;
   }

   public List<RuleParam> getParams() {
      return params;
   }

   public void setParams(List<RuleParam> params) {
      this.params = params;
   }

   @Override
   public String toString() {
      return "Rule{" +
             "key='" + key + '\'' +
             ", name='" + name + '\'' +
             ", description='" + description + '\'' +
             ", configKey='" + configKey + '\'' +
             ", category=" + category +
             ", params=" + params +
             '}';
   }

}
