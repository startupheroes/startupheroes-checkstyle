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
   private String type;

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

   public String getType() {
      return type;
   }

   public void setType(String type) {
      this.type = type;
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
   public String toString() {
      return "RuleParam{" +
             "key='" + key + '\'' +
             ", type='" + type + '\'' +
             ", defaultValue='" + defaultValue + '\'' +
             ", description='" + description + '\'' +
             '}';
   }

}
