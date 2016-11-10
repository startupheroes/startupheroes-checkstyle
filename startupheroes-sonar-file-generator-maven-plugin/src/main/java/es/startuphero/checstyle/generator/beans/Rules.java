package es.startuphero.checstyle.generator.beans;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author ozlem.ulag
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Rules {

   @XmlElement(name = "rule")
   private List<Rule> rules = new ArrayList<>();

   public List<Rule> getRules() {
      return rules;
   }

   public void setRules(List<Rule> rules) {
      this.rules = rules;
   }

   @Override
   public String toString() {
      return "Rules{" +
             "rules=" + rules +
             '}';
   }

}
