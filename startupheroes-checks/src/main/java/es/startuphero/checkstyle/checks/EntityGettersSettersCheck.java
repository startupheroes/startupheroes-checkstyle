package es.startuphero.checkstyle.checks;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import es.startuphero.checkstyle.util.ClassUtil;
import es.startuphero.checkstyle.util.VariableUtil;
import es.startuphero.checkstyle.util.MethodUtil;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import static es.startuphero.checkstyle.util.VariableUtil.getVariableNameAstMap;

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

   private String abstractEntityAnnotation;

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
      assertions();
      if (ClassUtil.isEntity(ast, entityAnnotation) || ClassUtil.isEntity(ast, abstractEntityAnnotation)) {
         Map<String, DetailAST> variableNameAstMap = VariableUtil.getVariableNameAstMap(ast, false);
         List<String> getterVariableNames = MethodUtil.getGetters(ast).stream()
                                                      .map(getter -> MethodUtil.getMethodName(getter).split(MethodUtil.GETTER_PREFIX_REGEX)[1])
                                                      .collect(Collectors.toList());

         List<String> setterVariableNames = MethodUtil.getSetters(ast).stream()
                                                      .map(setter -> MethodUtil.getMethodName(setter).split(MethodUtil.SETTER_PREFIX_REGEX)[1])
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

   private void assertions() {
      Assert.isTrue(!StringUtils.isEmpty(entityAnnotation));
      Assert.isTrue(!StringUtils.isEmpty(abstractEntityAnnotation));
   }

   public void setEntityAnnotation(String entityAnnotation) {
      this.entityAnnotation = entityAnnotation;
   }

   public void setAbstractEntityAnnotation(String abstractEntityAnnotation) {
      this.abstractEntityAnnotation = abstractEntityAnnotation;
   }

}
