package es.startuphero.checkstyle.sonar;

import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.api.server.rule.RulesDefinitionXmlLoader;

/**
 * @author ozlem.ulag
 */
public class BaseRuleTestSupport {

   public static RulesDefinition.Repository getRepositoryOfRules() {
      SonarRulesDefinition rulesDefinition = new SonarRulesDefinition(new RulesDefinitionXmlLoader());
      RulesDefinition.Context context = new RulesDefinition.Context();
      rulesDefinition.define(context);
      return context.repository(SonarRulesDefinition.REPOSITORY_KEY);
   }

}
