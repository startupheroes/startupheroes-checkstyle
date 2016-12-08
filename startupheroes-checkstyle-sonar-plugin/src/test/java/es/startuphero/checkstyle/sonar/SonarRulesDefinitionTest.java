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

    assertEquals(repository.key(), "checkstyle");
    assertEquals(repository.name(), "Checkstyle");
    assertEquals(repository.language(), "java");
    assertEquals(repository.rules().isEmpty(), false);

    assertRuleProperties(repository);
    assertParameterProperties(repository);
    assertAllRuleParametersHaveDescription(repository);
  }

  private void assertRuleProperties(Repository repository) {
    Rule rule = repository.rule("es.startuphero.checkstyle.checks.modifier.MissingAbstractModifierCheck");
    assertNotNull(rule);
    assertEquals(rule.name(), "Missing Abstract Modifier Check");
    assertEquals(rule.htmlDescription(), "Missing Abstract Modifier Check");
    assertEquals(rule.internalKey(),
                 "Checker/TreeWalker/"
                 + "es.startuphero.checkstyle.checks.modifier.MissingAbstractModifierCheck");
    assertEquals(rule.type(), RuleType.CODE_SMELL);
    assertEquals(rule.severity(), Severity.MAJOR);
  }

  private void assertParameterProperties(Repository repository) {
    Param abstractTypeAnnotation =
        repository.rule("es.startuphero.checkstyle.checks.modifier.MissingAbstractModifierCheck")
                  .param("abstractTypeAnnotation");
    assertNotNull(abstractTypeAnnotation);
    assertEquals(abstractTypeAnnotation.name(), "abstractTypeAnnotation");
    assertEquals(abstractTypeAnnotation.defaultValue(), "javax.persistence.MappedSuperclass");
    assertEquals(abstractTypeAnnotation.description(),
                 "Abstract Type Annotation property with value of 'javax.persistence.MappedSuperclass'");
    assertEquals(abstractTypeAnnotation.type(), RuleParamType.STRING);
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
