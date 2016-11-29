package es.startuphero.checstyle.generator;

import es.startuphero.checstyle.generator.beans.Module;
import es.startuphero.checstyle.generator.beans.ModuleMetadata;
import es.startuphero.checstyle.generator.beans.ModuleProperty;
import es.startuphero.checstyle.generator.beans.Rule;
import es.startuphero.checstyle.generator.beans.RuleParam;
import es.startuphero.checstyle.generator.beans.Rules;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.sax.SAXSource;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

/**
 * Generate sonar rules xml file from checkers modules file.
 */
@Mojo(name = "generate-rules", threadSafe = true)
@SuppressWarnings("unused")
public class SonarRulesGeneratorMojo extends AbstractMojo {

  private static final String EXTERNAL_DTD_LOADING_FEATURE =
      "http://apache.org/xml/features/nonvalidating/load-external-dtd";

  private static final String SAX_FEATURES_VALIDATION = "http://xml.org/sax/features/validation";

  private static final String DEFAULT_RULE_TAG = "sh-rule";

  private static final String SKIPPED_MODULE_METADATA_NAME = "skip";

  /** Checkers xml file contains modules for each check **/
  @Parameter(
      defaultValue = "${basedir}/startupheroes-checks/src/main/resources/es/startuphero/checkstyle/checks"
                     + "/startupheroes_checks.xml")
  private String inputFile;

  /** Output file to generate Sonar rules xml file from Checker file **/
  @Parameter(
      defaultValue = "${basedir}/startupheroes-checkstyle-sonar-plugin/src/main/resources/es/startuphero/checkstyle"
                     + "/sonar/startupheroes_rules.xml")
  private String outputFile;

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    try {
      JAXBContext jaxbContext = JAXBContext.newInstance(Rules.class);
      Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
      // output pretty printed
      jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
      jaxbMarshaller.marshal(createRules(), getOutputFile());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private Rules createRules() {
    Module root = parseXmlToObject(); // root : Checker
    Rules rules = new Rules();
    addNewRule(rules, root);
    for (Module child : root.getChilds()) {
      child.setParent(root);
      addNewRule(rules, child);
      // suppose maximum 2 level modules (childs of TreeWalker)!
      for (Module secondLevelChild : child.getChilds()) {
        secondLevelChild.setParent(child);
        addNewRule(rules, secondLevelChild);
      }
    }
    return rules;
  }

  private Module parseXmlToObject() {
    Module module = null;
    try {
      JAXBContext jc = JAXBContext.newInstance(Module.class);

      SAXParserFactory spf = SAXParserFactory.newInstance();
      spf.setFeature(EXTERNAL_DTD_LOADING_FEATURE, false);
      spf.setFeature(SAX_FEATURES_VALIDATION, false);

      XMLReader xmlReader = spf.newSAXParser().getXMLReader();
      InputSource inputSource = new InputSource(new FileReader(new File(inputFile)));
      SAXSource source = new SAXSource(xmlReader, inputSource);

      Unmarshaller unmarshaller = jc.createUnmarshaller();
      module = (Module) unmarshaller.unmarshal(source);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return module;
  }

  /** skip Checker and TreeWalker modules **/
  private static void addNewRule(Rules rules, Module module) {
    if (module.getChilds().isEmpty() && acceptableRule(module)) {
      Rule newRule = convertModuleToRule(module);
      checkAlreadyExistingRule(rules, newRule);
      rules.getRules().add(newRule);
    }
  }

  private static Boolean acceptableRule(Module module) {
    return !module.getMetadatas()
                  .stream()
                  .map(ModuleMetadata::getName)
                  .collect(Collectors.toList())
                  .contains(SKIPPED_MODULE_METADATA_NAME);
  }

  private static Rule convertModuleToRule(Module module) {
    Rule rule = new Rule();
    rule.setKey(module.getName());
    rule.setName(getSeparatedString(module.getName()));
    rule.setDescription(getSeparatedString(module.getName()));
    rule.setInternalKey(getConfigKey(module));
    rule.getTags().add(DEFAULT_RULE_TAG);
    module.getProperties()
          .forEach(property -> rule.getParams()
                                   .add(convertModulePropertyToRuleParam(property)));
    return rule;
  }

  private static RuleParam convertModulePropertyToRuleParam(ModuleProperty property) {
    RuleParam param = new RuleParam();
    param.setKey(property.getName());
    param.setName(getSeparatedString(property.getName()));
    param.setDefaultValue(property.getValue());
    param.setDescription(getSeparatedString(property.getName())
                         + " property with value of '"
                         + property.getValue()
                         + "'");
    return param;
  }

  private static String getConfigKey(Module module) {
    String configKey = module.getName();
    Module parent = module.getParent();
    while (parent != null) {
      configKey = parent.getName() + "/" + configKey;
      parent = parent.getParent();
    }
    return configKey;
  }

  private static String getSimpleName(String fullName) {
    String[] packageNames = fullName.split("\\.");
    return packageNames[packageNames.length - 1];
  }

  private static String getSeparatedString(String input) {
    String[] splittedModuleNames = getSimpleName(input).split("(?=[A-Z])");
    StringBuilder builder = new StringBuilder();
    Arrays.stream(splittedModuleNames).forEach(s -> builder.append(s).append(" "));
    return capitalizeAllString(builder.toString().trim());
  }

  private static String capitalizeAllString(String input) {
    StringBuilder res = new StringBuilder();
    String[] strArr = input.split(" ");
    for (String str : strArr) {
      char[] stringArray = str.trim().toCharArray();
      stringArray[0] = Character.toUpperCase(stringArray[0]);
      str = new String(stringArray);

      res.append(str).append(" ");
    }
    return res.toString().trim();
  }

  private static void checkAlreadyExistingRule(Rules rules, Rule newRule) {
    Boolean alreadyExistRule = isAlreadyExistRule(rules, newRule);
    Optional<Rule> existedRule = rules.getRules()
                                      .stream()
                                      .filter(rule -> rule.getKey().equals(newRule.getKey()))
                                      .findFirst();
    int uniqueCounter = 2;
    while (alreadyExistRule) {
      newRule.setKey(existedRule.get().getKey() + "-" + uniqueCounter);
      uniqueCounter++;
      alreadyExistRule = isAlreadyExistRule(rules, newRule);
    }
  }

  private static Boolean isAlreadyExistRule(Rules rules, Rule newRule) {
    return rules.getRules()
                .stream()
                .anyMatch(rule -> rule.getKey().equals(newRule.getKey()));
  }

  private File getOutputFile() {
    File outputFile = new File(this.outputFile);
    if (!outputFile.exists()) {
      try {
        outputFile.getParentFile().mkdirs();
        outputFile.createNewFile();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return outputFile;
  }
}
