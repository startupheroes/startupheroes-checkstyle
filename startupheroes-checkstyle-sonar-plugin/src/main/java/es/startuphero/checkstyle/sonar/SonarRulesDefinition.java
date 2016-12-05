package es.startuphero.checkstyle.sonar;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.api.server.rule.RulesDefinitionXmlLoader;

/**
 * @author ozlem.ulag
 */
public final class SonarRulesDefinition implements RulesDefinition {

  /** DONT CHANGE THESE VALUES! **/
  public static final String REPOSITORY_KEY = "checkstyle";

  private static final String REPOSITORY_NAME = "Checkstyle";

  private static final String REPOSITORY_LANGUAGE = "java";

  private static final String RULES_RELATIVE_FILE_PATH = "/es/startuphero/checkstyle/sonar/startupheroes_rules.xml";

  private RulesDefinitionXmlLoader xmlLoader;

  public SonarRulesDefinition(RulesDefinitionXmlLoader xmlLoader) {
    this.xmlLoader = xmlLoader;
  }

  @Override
  public void define(Context context) {
    try (Reader reader = new InputStreamReader(
        getClass().getResourceAsStream(RULES_RELATIVE_FILE_PATH), StandardCharsets.UTF_8)) {
      NewRepository repository = context.createRepository(REPOSITORY_KEY, REPOSITORY_LANGUAGE).setName(REPOSITORY_NAME);
      xmlLoader.load(repository, reader);
      repository.done();
    } catch (IOException e) {
      throw new IllegalStateException(
          String.format("Fail to read file %s", RULES_RELATIVE_FILE_PATH), e);
    }
  }
}
