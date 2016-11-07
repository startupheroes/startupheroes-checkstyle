package es.startuphero.checkstyle.checks;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import es.startuphero.checkstyle.util.ClassUtil;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * @author ozlem.ulag
 */
public class AbstractEntityNameCheck extends AbstractCheck {

   /**
    * A key is pointing to the warning message text in "messages.properties" file.
    */
   private static final String MSG_KEY = "abstractEntityNameCheckMessage";

   /**
    * set abstract entity annotation to understand that a class is an entity.
    */
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
      if (ClassUtil.isEntity(ast, abstractEntityAnnotation)) {
         String className = ClassUtil.getClassName(ast);
         if (!className.startsWith(ClassUtil.ABSTRACT_CLASS_PREFIX)) {
            log(ast.getLineNo(), MSG_KEY);
         }
      }
   }

   private void assertions() {
      Assert.isTrue(!StringUtils.isEmpty(abstractEntityAnnotation));
   }

   public void setAbstractEntityAnnotation(String abstractEntityAnnotation) {
      this.abstractEntityAnnotation = abstractEntityAnnotation;
   }

}
