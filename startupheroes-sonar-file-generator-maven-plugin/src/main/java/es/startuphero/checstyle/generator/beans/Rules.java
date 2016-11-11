package es.startuphero.checstyle.generator.beans;

import java.util.LinkedHashSet;
import java.util.Set;
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
   private Set<Rule> rules = new LinkedHashSet<>();

   public Set<Rule> getRules() {
      return rules;
   }

   public void setRules(Set<Rule> rules) {
      this.rules = rules;
   }

   @Override
   public String toString() {
      return "Rules{" +
             "rules=" + rules +
             '}';
   }

}
