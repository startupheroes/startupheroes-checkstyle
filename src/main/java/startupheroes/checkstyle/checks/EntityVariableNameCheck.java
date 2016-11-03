package startupheroes.checkstyle.checks;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import java.util.Map;
import org.springframework.util.Assert;

import static org.springframework.util.StringUtils.isEmpty;
import static startupheroes.checkstyle.util.ClassUtil.getClassName;
import static startupheroes.checkstyle.util.ClassUtil.isEntity;
import static startupheroes.checkstyle.util.CommonUtil.getNameWithoutContext;
import static startupheroes.checkstyle.util.VariableUtil.getVariableNameAstMap;

/**
 * @author ozlem.ulag
 */
public class EntityVariableNameCheck extends AbstractCheck {

   /**
    * A key is pointing to the warning message text in "messages.properties" file.
    */
   private static final String MSG_KEY = "entityVariableNameCheckMessage";

   private static final String ABSTRACT_CLASS_PREFIX = "Abstract";

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
      if (isEntity(ast, entityAnnotation) || isEntity(ast, abstractEntityAnnotation)) {
         String className = getClassName(ast);
         if (className.startsWith(ABSTRACT_CLASS_PREFIX)) {
            className = getNameWithoutContext(className, ABSTRACT_CLASS_PREFIX);
         }
         checkVariablesName(ast, className);
      }
   }

   private void assertions() {
      Assert.isTrue(!isEmpty(entityAnnotation));
      Assert.isTrue(!isEmpty(abstractEntityAnnotation));
   }

   private void checkVariablesName(DetailAST ast, String className) {
      Map<String, DetailAST> variableNameAstMap = getVariableNameAstMap(ast, false);
      for (String variableName : variableNameAstMap.keySet()) {
         Boolean variableNameInContextOfClassName = variableName.toLowerCase().contains(className.toLowerCase());
         String suggestedVariableName = getNameWithoutContext(variableName, className);
         if (variableNameInContextOfClassName && !suggestedVariableName.equals(variableName)) {
            log(variableNameAstMap.get(variableName).getLineNo(), MSG_KEY, suggestedVariableName);
         }
      }
   }

   public void setEntityAnnotation(String entityAnnotation) {
      this.entityAnnotation = entityAnnotation;
   }

   public void setAbstractEntityAnnotation(String abstractEntityAnnotation) {
      this.abstractEntityAnnotation = abstractEntityAnnotation;
   }

}
