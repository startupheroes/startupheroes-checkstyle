package es.startuphero.checstyle.generator.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * @author ozlem.ulag
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class RuleCategory {

   @XmlAttribute
   private String name;

   public RuleCategory(String name) {
      this.name = name;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   @Override
   public String toString() {
      return "RuleCategory{" +
             "name='" + name + '\'' +
             '}';
   }

}
