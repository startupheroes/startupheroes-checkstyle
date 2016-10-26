package startupheroes.checkstyle.checks.custom;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import com.puppycrawl.tools.checkstyle.utils.CheckUtils;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import startupheroes.checkstyle.util.CommonUtil;

import static startupheroes.checkstyle.util.CommonUtil.getMethods;
import static startupheroes.checkstyle.util.CommonUtil.getVariableNameAstMap;

/**
 * @author ozlem.ulag
 */
public class EntityGettersSettersCheck extends AbstractCheck {

   /**
    * A key is pointing to the warning message text in "messages.properties"
    * file.
    */
   static final String MSG_KEY = "entityGettersSettersCheckMessage";

   /** Pattern matching names of getter methods. */
   private static final String GETTER_PREFIX_REGEX = "^get";

   /** Pattern matching names of setter methods. */
   private static final String SETTER_PREFIX_REGEX = "^set";

   /**
    * set possible annotations to understand that a class is an entity.
    */
   private Set<String> entityAnnotations = new HashSet<>();

   @Override
   public int[] getDefaultTokens() {
      return new int[]{TokenTypes.CLASS_DEF};
   }

   @Override
   public void visitToken(DetailAST ast) {
      if (CommonUtil.isEntity(entityAnnotations, ast)) {
         List<DetailAST> methods = getMethods(ast);
         List<DetailAST> getters = methods.stream().filter(CheckUtils::isGetterMethod).collect(Collectors.toList());
         List<DetailAST> setters = methods.stream().filter(CheckUtils::isSetterMethod).collect(Collectors.toList());

         Map<String, DetailAST> variableNameAstMap = getVariableNameAstMap(ast, false);
         List<String> getterVariableNames = getters.stream()
                                                   .map(getter -> CommonUtil.getMethodName(getter).split(GETTER_PREFIX_REGEX)[1])
                                                   .collect(Collectors.toList());

         List<String> setterVariableNames = setters.stream()
                                                   .map(setter -> CommonUtil.getMethodName(setter).split(SETTER_PREFIX_REGEX)[1])
                                                   .collect(Collectors.toList());

         for (String variableName : variableNameAstMap.keySet()) {
            String nameInGettersAndSetters = variableName.substring(0, 1).toUpperCase() + variableName.substring(1);
            if (!getterVariableNames.contains(nameInGettersAndSetters) ||
                !setterVariableNames.contains(nameInGettersAndSetters)) {
               log(variableNameAstMap.get(variableName).getLineNo(), MSG_KEY, variableName);
            }
         }
      }
   }

   public void setEntityAnnotations(String... entityAnnotations) {
      Collections.addAll(this.entityAnnotations, entityAnnotations);
   }

}
