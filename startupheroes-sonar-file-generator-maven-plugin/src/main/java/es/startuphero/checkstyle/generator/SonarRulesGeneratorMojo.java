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
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
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

import static java.util.stream.Collectors.toCollection;

/**
 * @author ozlem.ulag
 *
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

  private static final String TEMPLATE_CARDINALITY = "MULTIPLE";

  /** Checkers xml file contains modules for each check **/
  @Parameter(
      defaultValue = "${basedir}/../startupheroes-checks/src/main/resources/es/startuphero/checkstyle/"
                     + "startupheroes_checks.xml")
  private String inputFile;

  /** Output file to generate Sonar rules xml file from Checker file **/
  @Parameter(
      defaultValue = "${basedir}/src/main/resources/es/startuphero/checkstyle/sonar/startupheroes_rules.xml")
  private String outputFile;

  /** skip Checker and TreeWalker modules **/
  private static void addNewRule(Rules rules, Module module) {
    if (module.getChilds().isEmpty() && !skippedRule(module)) {
      Rule newRule = convertModuleToRule(module);
      if (!checkAlreadyExistRule(rules, newRule)) {
        rules.getRules().add(newRule);
      }
    }
  }

  private static Boolean skippedRule(Module module) {
    return module.getMetadatas()
                 .stream()
                 .map(ModuleMetadata::getName)
                 .collect(Collectors.toList())
                 .contains(SKIPPED_MODULE_METADATA_NAME);
  }

  private static Rule convertModuleToRule(Module module) {
    Rule rule = new Rule();
    String moduleName = module.getName();
    rule.setKey(moduleName);
    rule.setInternalKey(getInternalKey(module));
    rule.setName(getSeparatedString(rule.getKey()));
    rule.setDescription(getSeparatedString(rule.getKey()));
    rule.getTags().add(DEFAULT_RULE_TAG);
    Optional<String> categoryPackageName = getCategoryPackageName(moduleName);
    categoryPackageName.ifPresent(s -> rule.getTags().add(s));
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

  private static String getInternalKey(Module module) {
    String internalKey = module.getName();
    Module parent = module.getParent();
    while (parent != null) {
      internalKey = parent.getName() + "/" + internalKey;
      parent = parent.getParent();
    }
    return internalKey;
  }

  private static String getSimpleName(String fullName) {
    String[] packageNames = fullName.split("\\.");
    return packageNames[packageNames.length - 1];
  }

  private static Optional<String> getCategoryPackageName(String fullName) {
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

  private static Boolean checkAlreadyExistRule(Rules rules, Rule newRule) {
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

  private static Boolean isAlreadyExistRule(Rules rules, Rule newRule) {
    return rules.getRules()
                .stream()
                .anyMatch(rule -> rule.getKey().equals(newRule.getKey()));
  }

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    try {
      JAXBContext jaxbContext = JAXBContext.newInstance(Rules.class);
      Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
      jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
      Rules rules = createRules();
      sortRulesByAlphabetically(rules);
      jaxbMarshaller.marshal(rules, getOutputFile());
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

  private void sortRulesByAlphabetically(Rules rules) {
    Set<Rule> sortedRules = rules.getRules()
                                 .stream()
                                 .sorted(Comparator.comparing(Rule::getKey))
                                 .collect(toCollection(LinkedHashSet::new));
    rules.setRules(sortedRules);
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
