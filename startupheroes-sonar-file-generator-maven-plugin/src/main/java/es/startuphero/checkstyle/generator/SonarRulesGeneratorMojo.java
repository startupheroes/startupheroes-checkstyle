package es.startuphero.checkstyle.generator;

import es.startuphero.checkstyle.generator.beans.Module;
import es.startuphero.checkstyle.generator.beans.ModuleMetadata;
import es.startuphero.checkstyle.generator.beans.ModuleProperty;
import es.startuphero.checkstyle.generator.beans.Rule;
import es.startuphero.checkstyle.generator.beans.RuleParam;
import es.startuphero.checkstyle.generator.beans.Rules;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toList;

/**
 * @author ozlem.ulag
 *
 * Generate sonar rules xml file from checkers modules file.
 *
 * Checkstyle Checks XML file -> Sonar Rules XML file
 */
@Mojo(name = "generate-rules", threadSafe = true)
@SuppressWarnings("unused")
public class SonarRulesGeneratorMojo extends AbstractMojo {

  private static final Logger LOGGER = LoggerFactory.getLogger(SonarRulesGeneratorMojo.class);

  private static final String EXTERNAL_DTD_LOADING_FEATURE =
      "http://apache.org/xml/features/nonvalidating/load-external-dtd";

  private static final String SAX_FEATURES_VALIDATION = "http://xml.org/sax/features/validation";

  private static final String DEFAULT_RULE_TAG = "sh-rule";

  private static final String DEFAULT_RULE_SEVERITY = "MAJOR";

  private static final String TEMPLATE_CARDINALITY = "MULTIPLE";

  private static final String SKIPPED_MODULE_METADATA_NAME = "skip";

  private static final String MAIN_PACKAGE = "src/main/resources/es/startuphero/checkstyle";

  /**
   * Checkers xml file contains modules for each check
   */
  @Parameter(defaultValue = "${basedir}/../startupheroes-checks/" + MAIN_PACKAGE + "/startupheroes_checks.xml")
  private String inputFile;

  /**
   * Output file to generate Sonar rules xml file from Checker file
   */
  @Parameter(defaultValue = "${basedir}/" + MAIN_PACKAGE + "/sonar/startupheroes_rules.xml")
  private String outputFile;

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    try {
      JAXBContext jaxbContext = JAXBContext.newInstance(Rules.class);
      Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
      jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
      Rules rules = createRules();
      sortRulesByAlphabetically(rules);
      jaxbMarshaller.marshal(rules, getOutputFile());
    } catch (Exception ex) {
      LOGGER.error("An error occurred while generating rules file, ", ex);
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
    } catch (Exception ex) {
      LOGGER.error("An error occurred while parsing xml file, ", ex);
    }
    return module;
  }

  /**
   * skip Checker and TreeWalker modules
   */
  private void addNewRule(Rules rules, Module module) {
    if (module.getChilds().isEmpty() && !skippedRule(module)) {
      Rule newRule = convertModuleToRule(module);
      if (!checkAlreadyExistRule(rules, newRule)) {
        rules.getRules().add(newRule);
      }
    }
  }

  private Boolean skippedRule(Module module) {
    return module.getMetadatas()
                 .stream()
                 .map(ModuleMetadata::getName)
                 .collect(toList())
                 .contains(SKIPPED_MODULE_METADATA_NAME);
  }

  private Rule convertModuleToRule(Module module) {
    Rule rule = new Rule();
    String moduleName = module.getName();
    rule.setKey(moduleName);
    rule.setInternalKey(getInternalKey(module));
    rule.setName(getSeparatedString(rule.getKey()));
    rule.setDescription(getSeparatedString(rule.getKey()));
    rule.setSeverity(DEFAULT_RULE_SEVERITY);
    rule.getTags().add(DEFAULT_RULE_TAG);
    Optional<String> categoryPackageName = getCategoryPackageName(moduleName);
    categoryPackageName.ifPresent(s -> rule.getTags().add(s));
    module.getProperties()
          .forEach(property -> rule.getParams()
                                   .add(convertModulePropertyToRuleParam(property)));
    return rule;
  }

  private RuleParam convertModulePropertyToRuleParam(ModuleProperty property) {
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

  private String getInternalKey(Module module) {
    String internalKey = module.getName();
    Module parent = module.getParent();
    while (parent != null) {
      internalKey = parent.getName().concat("/").concat(internalKey);
      parent = parent.getParent();
    }
    return internalKey;
  }

  private String getSimpleName(String fullName) {
    String[] packageNames = fullName.split("\\.");
    return packageNames[packageNames.length - 1];
  }

  private Optional<String> getCategoryPackageName(String fullName) {
    Optional<String> result = Optional.empty();
    String[] packageNames = fullName.split("\\.");
    int indexOfCategoryPackageName = packageNames.length - 2;
    int indexOfCheckerPackageName = indexOfCategoryPackageName - 1;
    if (indexOfCheckerPackageName >= 0 &&
        packageNames[indexOfCheckerPackageName].equals("checks")) {
      result = Optional.of(packageNames[indexOfCategoryPackageName]);
    }
    return result;
  }

  private String getSeparatedString(String input) {
    String[] splittedModuleNames = getSimpleName(input).split("(?=[A-Z])");
    StringBuilder builder = new StringBuilder();
    Arrays.stream(splittedModuleNames).forEach(s -> builder.append(s).append(" "));
    return capitalizeAllString(builder.toString().trim());
  }

  private String capitalizeAllString(String input) {
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

  private Boolean checkAlreadyExistRule(Rules rules, Rule newRule) {
    Boolean alreadyExistRule = isAlreadyExistRule(rules, newRule);
    if (alreadyExistRule) {
      Rule existedRule = rules.getRules()
                              .stream()
                              .filter(rule -> rule.getKey().equals(newRule.getKey()))
                              .findFirst().get();
      existedRule.setCardinality(TEMPLATE_CARDINALITY);
    }
    return alreadyExistRule;
  }

  private Boolean isAlreadyExistRule(Rules rules, Rule newRule) {
    return rules.getRules()
                .stream()
                .anyMatch(rule -> rule.getKey().equals(newRule.getKey()));
  }

  private void sortRulesByAlphabetically(Rules rules) {
    Set<Rule> sortedRules = rules.getRules()
                                 .stream()
                                 .sorted(comparing(Rule::getKey))
                                 .collect(toCollection(LinkedHashSet::new));
    rules.setRules(sortedRules);
  }

  private File getOutputFile() throws IOException {
    File outputFile = new File(this.outputFile);
    if (!outputFile.exists()) {
      outputFile.getParentFile().mkdirs();
      outputFile.createNewFile();
    }
    return outputFile;
  }
}
