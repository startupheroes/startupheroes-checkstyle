package startupheroes.checkstyle.checks;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import java.util.Map;

import static java.util.Objects.nonNull;
import static startupheroes.checkstyle.util.ClassUtil.getClassName;
import static startupheroes.checkstyle.util.ClassUtil.isEntity;
import static startupheroes.checkstyle.util.VariableUtil.getVariableNameAstMap;

/**
 * @author ozlem.ulag
 */
public class EntityVariableNameCheck extends AbstractCheck {

   /**
    * A key is pointing to the warning message text in "messages.properties" file.
    */
   private static final String MSG_KEY = "entityVariableNameCheckMessage";

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
      if (isEntity(ast, entityAnnotation)) {
         String className = getClassName(ast);
         Map<String, DetailAST> variableNameAstMap = getVariableNameAstMap(ast);
         for (String variableName : variableNameAstMap.keySet()) {
            Boolean variableNameInContextOfClassName = variableName.toLowerCase().startsWith(className.toLowerCase());
            if (variableNameInContextOfClassName) {
               log(variableNameAstMap.get(variableName).getLineNo(), MSG_KEY, getSuggestedVariableName(className, variableName));
            }
         }
      }
   }

   private String getSuggestedVariableName(String className, String variableName) {
      String[] separated = variableName.split("(?i)" + className);
      String restWithoutClassName = separated[1];
      return nonNull(restWithoutClassName) ?
          restWithoutClassName.substring(0, 1).toLowerCase() + restWithoutClassName.substring(1) : "";
   }

   public void setEntityAnnotation(String entityAnnotation) {
      this.entityAnnotation = entityAnnotation;
   }

}
