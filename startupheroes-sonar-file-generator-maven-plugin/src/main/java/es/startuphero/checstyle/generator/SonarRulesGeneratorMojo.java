package es.startuphero.checstyle.generator;

import es.startuphero.checstyle.generator.beans.Module;
import es.startuphero.checstyle.generator.beans.ModuleProperty;
import es.startuphero.checstyle.generator.beans.Rule;
import es.startuphero.checstyle.generator.beans.RuleCategory;
import es.startuphero.checstyle.generator.beans.RuleParam;
import es.startuphero.checstyle.generator.beans.Rules;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * Generate sonar rules xml file from checkers modules file.
 */
@Mojo(name = "sonar-rules", defaultPhase = LifecyclePhase.NONE, requiresProject = true, threadSafe = true)
@SuppressWarnings("unused")
public class SonarRulesGeneratorMojo extends AbstractMojo {

   /** Checkers xml file contains modules for each check **/
   @Parameter(defaultValue = "${basedir}/startupheroes-checks/src/main/resources/es/startuphero/checkstyle/checks/startupheroes_checks.xml")
   private String inputFile;

   /** Output file to generate Sonar rules xml file from Checker file **/
   @Parameter(defaultValue = "${basedir}/startupheroes-checkstyle-sonar-plugin/src/main/resources/es/startuphero/checkstyle/sonar/startupheroes_rules.xml")
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
      Module root = parseXmlToObject(new File(inputFile)); // root : Checker
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

   private static Module parseXmlToObject(File inputFile) {
      Module module = null;
      try {
         JAXBContext jaxbContext = JAXBContext.newInstance(Module.class);

         Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
         module = (Module) jaxbUnmarshaller.unmarshal(inputFile);
      } catch (JAXBException e) {
         e.printStackTrace();
      }
      return module;
   }

   /** skip Checker and TreeWalker modules **/
   private static void addNewRule(Rules rules, Module module) {
      if (module.getChilds().isEmpty()) {
         rules.getRules().add(convertModuleToRule(module));
      }
   }

   private static Rule convertModuleToRule(Module module) {
      Rule rule = new Rule();
      rule.setKey(module.getName());
      rule.setName(getSeparatedModuleName(module.getName()));
      rule.setDescription(getSeparatedModuleName(module.getName()));
      rule.setConfigKey(getConfigKey(module));
      rule.setCategory(new RuleCategory("coding"));
      module.getProperties()
            .forEach(property -> rule.getParams()
                                     .add(convertModulePropertyToRuleParam(property)));
      return rule;
   }

   private static RuleParam convertModulePropertyToRuleParam(ModuleProperty property) {
      RuleParam param = new RuleParam();
      param.setKey(property.getName());
      param.setDefaultValue(property.getValue());
      param.setType("STRING");
      param.setDescription(getSeparatedModuleName(property.getName()));
      return param;
   }

   private static String getSeparatedModuleName(String name) {
      String[] splittedModuleNames = getSimpleName(name).split("(?=[A-Z])");
      StringBuilder builder = new StringBuilder();
      Arrays.stream(splittedModuleNames).forEach(s -> builder.append(s).append(" "));
      return builder.toString().trim();
   }

   private static String getSimpleName(String fullName) {
      String[] packageNames = fullName.split("\\.");
      return packageNames[packageNames.length - 1];
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

}
