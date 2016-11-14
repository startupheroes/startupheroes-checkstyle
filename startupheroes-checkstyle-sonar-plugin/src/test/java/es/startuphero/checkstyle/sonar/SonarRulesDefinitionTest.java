package es.startuphero.checkstyle.sonar;

import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.api.server.rule.RuleParamType;
import org.sonar.api.server.rule.RulesDefinition.Param;
import org.sonar.api.server.rule.RulesDefinition.Repository;
import org.sonar.api.server.rule.RulesDefinition.Rule;

import static es.startuphero.checkstyle.sonar.BaseRuleTestSupport.getRepositoryOfRules;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SonarRulesDefinitionTest {

   @Test
   public void test() {
      Repository repository = getRepositoryOfRules();

      assertEquals(repository.name(), "SH-Checkstyle");
      assertEquals(repository.language(), "java");
      assertEquals(repository.rules().isEmpty(), false);

      assertRuleProperties(repository);
      assertParameterProperties(repository);
      assertAllRuleParametersHaveDescription(repository);
   }

   private void assertRuleProperties(Repository repository) {
      Rule rule = repository.rule("es.startuphero.checkstyle.checks.AbstractEntityModifierCheck");
      assertNotNull(rule);
      assertEquals(rule.name(), "Abstract Entity Modifier Check");
      assertEquals(rule.htmlDescription(), "Abstract Entity Modifier Check");
      assertEquals(rule.internalKey(), "Checker/TreeWalker/es.startuphero.checkstyle.checks.AbstractEntityModifierCheck");
      assertEquals(rule.type(), RuleType.CODE_SMELL);
      assertEquals(rule.severity(), Severity.MAJOR);
   }

   private void assertParameterProperties(Repository repository) {
      Param abstractEntityAnnotation = repository.rule("es.startuphero.checkstyle.checks.AbstractEntityModifierCheck")
                                                 .param("abstractEntityAnnotation");
      assertNotNull(abstractEntityAnnotation);
      assertEquals(abstractEntityAnnotation.name(), "abstractEntityAnnotation");
      assertEquals(abstractEntityAnnotation.defaultValue(), "javax.persistence.MappedSuperclass");
      assertEquals(abstractEntityAnnotation.description(), "Abstract Entity Annotation property with value of 'javax.persistence.MappedSuperclass'");
      assertEquals(abstractEntityAnnotation.type(), RuleParamType.STRING);
   }

   private void assertAllRuleParametersHaveDescription(Repository repository) {
      for (Rule rule : repository.rules()) {
         for (Param param : rule.params()) {
            assertNotNull(param.description());
            assertNotNull(param.key());
         }
      }
   }

}
