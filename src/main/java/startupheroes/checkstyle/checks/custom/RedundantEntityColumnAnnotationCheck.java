package startupheroes.checkstyle.checks.custom;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import com.puppycrawl.tools.checkstyle.utils.AnnotationUtility;

/**
 * @author ozlem.ulag
 */
public class RedundantEntityColumnAnnotationCheck extends AbstractCheck {

   /**
    * A key is pointing to the warning message text in "messages.properties"
    * file.
    */
   static final String MSG_KEY = "redundantEntityColumnAnnotationCheckMessage";

   private static final String ID_ANNOTATION = "Id";

   /** Fully-qualified javax.persistence.Id annotation name. */
   private static final String FQ_ID_ANNOTATION = "javax.persistence." + ID_ANNOTATION;

   private static final String COLUMN_ANNOTATION = "Column";

   /** Fully-qualified javax.persistence.Column annotation name. */
   private static final String FQ_COLUMN_ANNOTATION = "javax.persistence." + COLUMN_ANNOTATION;

   @Override
   public int[] getDefaultTokens() {
      return getRequiredTokens();
   }

   @Override
   public int[] getAcceptableTokens() {
      return getRequiredTokens();
   }

   @Override
   public int[] getRequiredTokens() {
      return new int[]{TokenTypes.VARIABLE_DEF};
   }

   @Override
   public void visitToken(final DetailAST ast) {
      if ((AnnotationUtility.containsAnnotation(ast, ID_ANNOTATION) ||
           AnnotationUtility.containsAnnotation(ast, FQ_ID_ANNOTATION)) &&
          (AnnotationUtility.containsAnnotation(ast, COLUMN_ANNOTATION) ||
           AnnotationUtility.containsAnnotation(ast, FQ_COLUMN_ANNOTATION))) {
         log(ast.getLineNo(), MSG_KEY);
      }
   }

}
