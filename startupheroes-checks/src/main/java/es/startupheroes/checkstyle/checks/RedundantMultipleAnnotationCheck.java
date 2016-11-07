package es.startupheroes.checkstyle.checks;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import es.startupheroes.checkstyle.util.AnnotationUtil;
import es.startupheroes.checkstyle.util.CommonUtil;
import java.util.HashMap;
import java.util.Map;

import static es.startupheroes.checkstyle.util.CommonUtil.getSimpleName;

/**
 * @author ozlem.ulag
 */
public class RedundantMultipleAnnotationCheck extends AbstractCheck {

   /**
    * A key is pointing to the warning message text in "messages.properties" file.
    */
   private static final String MSG_KEY = "redundantMultipleAnnotationCheckMessage";

   private Map<String, String> redundantAnnotationPairs = new HashMap<>();

   @Override
   public int[] getDefaultTokens() {
      return getAcceptableTokens();
   }

   @Override
   public int[] getAcceptableTokens() {
      return new int[]{
          TokenTypes.CLASS_DEF,
          TokenTypes.INTERFACE_DEF,
          TokenTypes.ENUM_DEF,
          TokenTypes.METHOD_DEF,
          TokenTypes.CTOR_DEF,
          TokenTypes.VARIABLE_DEF,
          };
   }

   @Override
   public int[] getRequiredTokens() {
      return getAcceptableTokens();
   }

   @Override
   public void visitToken(DetailAST ast) {
      for (String annotation1 : redundantAnnotationPairs.keySet()) {
         String annotation2 = redundantAnnotationPairs.get(annotation1);
         if (AnnotationUtil.hasAnnotation(ast, annotation1) && AnnotationUtil.hasAnnotation(ast, annotation2)) {
            log(ast.getLineNo(), MSG_KEY, CommonUtil.getSimpleName(annotation1), CommonUtil.getSimpleName(annotation2));
         }
      }
   }

   public void setRedundantAnnotationPairs(String property) {
      this.redundantAnnotationPairs = CommonUtil.splitProperty(property);
   }

}
