package startupheroes.checkstyle.checks;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import static startupheroes.checkstyle.util.ClassUtil.isEntity;
import static startupheroes.checkstyle.util.MethodUtil.GETTER_PREFIX_REGEX;
import static startupheroes.checkstyle.util.MethodUtil.SETTER_PREFIX_REGEX;
import static startupheroes.checkstyle.util.MethodUtil.getGetters;
import static startupheroes.checkstyle.util.MethodUtil.getMethodName;
import static startupheroes.checkstyle.util.MethodUtil.getSetters;
import static startupheroes.checkstyle.util.VariableUtil.getVariableNameAstMap;

/**
 * @author ozlem.ulag
 */
public class EntityGettersSettersCheck extends AbstractCheck {

   /**
    * A key is pointing to the warning message text in "messages.properties" file.
    */
   private static final String MSG_KEY = "entityGettersSettersCheckMessage";

   /**
    * set entity annotation to understand that a class is an entity.
    */
   private String entityAnnotation;

   @Override
   public int[] getDefaultTokens() {
      return getAcceptableTokens();
   }

   @Override
   public int[] getAcceptableTokens() {
      return new int[]{TokenTypes.CLASS_DEF};
   }

   @Override
   public int[] getRequiredTokens() {
      return getAcceptableTokens();
   }

   @Override
   public void visitToken(DetailAST ast) {
      Assert.isTrue(!StringUtils.isEmpty(entityAnnotation));
      if (isEntity(ast, entityAnnotation)) {
         Map<String, DetailAST> variableNameAstMap = getVariableNameAstMap(ast);
         List<String> getterVariableNames = getGetters(ast).stream()
                                                           .map(getter -> getMethodName(getter).split(GETTER_PREFIX_REGEX)[1])
                                                           .collect(Collectors.toList());

         List<String> setterVariableNames = getSetters(ast).stream()
                                                           .map(setter -> getMethodName(setter).split(SETTER_PREFIX_REGEX)[1])
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

   public void setEntityAnnotation(String entityAnnotation) {
      this.entityAnnotation = entityAnnotation;
   }

}
