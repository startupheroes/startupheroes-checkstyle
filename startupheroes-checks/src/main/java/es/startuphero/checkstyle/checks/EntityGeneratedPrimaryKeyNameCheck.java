package es.startuphero.checkstyle.checks;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import es.startuphero.checkstyle.util.AnnotationUtil;
import es.startuphero.checkstyle.util.ClassUtil;
import es.startuphero.checkstyle.util.VariableUtil;
import java.util.List;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

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

   private String abstractEntityAnnotation;

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
      assertions();
      if (ClassUtil.isEntity(ast, entityAnnotation) || ClassUtil.isEntity(ast, abstractEntityAnnotation)) {
         List<DetailAST> variables = VariableUtil.getNonStaticVariables(ast);
         for (DetailAST variable : variables) {
            if (AnnotationUtil.hasAnnotation(variable, idAnnotation) && AnnotationUtil.hasAnnotation(variable, generatedValueAnnotation)) {
               String generatedPrimaryKeyName = VariableUtil.getVariableName(variable);
               if (!generatedPrimaryKeyName.equals(suggestedGeneratedPrimaryKeyName)) {
                  log(variable.getLineNo(), MSG_KEY, suggestedGeneratedPrimaryKeyName);
               }
               break;
            }
         }
      }
   }

   private void assertions() {
      Assert.isTrue(!StringUtils.isEmpty(entityAnnotation));
      Assert.isTrue(!StringUtils.isEmpty(abstractEntityAnnotation));
      Assert.isTrue(!StringUtils.isEmpty(idAnnotation));
      Assert.isTrue(!StringUtils.isEmpty(generatedValueAnnotation));
      Assert.isTrue(!StringUtils.isEmpty(suggestedGeneratedPrimaryKeyName));
   }

   public void setEntityAnnotation(String entityAnnotation) {
      this.entityAnnotation = entityAnnotation;
   }

   public void setAbstractEntityAnnotation(String abstractEntityAnnotation) {
      this.abstractEntityAnnotation = abstractEntityAnnotation;
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
