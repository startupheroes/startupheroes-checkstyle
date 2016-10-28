package startupheroes.checkstyle.checks;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import java.util.List;

import static startupheroes.checkstyle.util.AnnotationUtil.hasAnnotation;
import static startupheroes.checkstyle.util.ClassUtil.isEntity;
import static startupheroes.checkstyle.util.VariableUtil.getVariableName;
import static startupheroes.checkstyle.util.VariableUtil.getVariables;

/**
 * @author ozlem.ulag
 */
public class EntityGeneratedPrimaryKeyNameCheck extends AbstractCheck {

   /**
    * A key is pointing to the warning message text in "messages.properties" file.
    */
   private static final String MSG_KEY = "entityGeneratedPrimaryKeyNameCheckMessage";

   /**
    * set entity annotation to understand that a class is an entity.
    */
   private String entityAnnotation;

   /**
    * set id annotation to understand that a variable is primary key of entity.
    */
   private String idAnnotation;

   /**
    * set generated value annotation to understand that primary key is generated.
    */
   private String generatedValueAnnotation;

   /**
    * set convenient name for variable for generated primary key.
    */
   private String suggestedGeneratedPrimaryKeyName;

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
         List<DetailAST> variables = getVariables(ast);
         for (DetailAST variable : variables) {
            if (hasAnnotation(variable, idAnnotation) && hasAnnotation(variable, generatedValueAnnotation)) {
               String generatedPrimaryKeyName = getVariableName(variable);
               if (!generatedPrimaryKeyName.equals(suggestedGeneratedPrimaryKeyName)) {
                  log(variable.getLineNo(), MSG_KEY, suggestedGeneratedPrimaryKeyName);
               }
               break;
            }
         }
      }
   }

   public void setEntityAnnotation(String entityAnnotation) {
      this.entityAnnotation = entityAnnotation;
   }

   public void setIdAnnotation(String idAnnotation) {
      this.idAnnotation = idAnnotation;
   }

   public void setGeneratedValueAnnotation(String generatedValueAnnotation) {
      this.generatedValueAnnotation = generatedValueAnnotation;
   }

   public void setSuggestedGeneratedPrimaryKeyName(String suggestedGeneratedPrimaryKeyName) {
      this.suggestedGeneratedPrimaryKeyName = suggestedGeneratedPrimaryKeyName;
   }

}
