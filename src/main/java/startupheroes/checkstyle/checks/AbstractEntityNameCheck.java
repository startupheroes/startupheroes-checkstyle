package startupheroes.checkstyle.checks;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import static startupheroes.checkstyle.util.ClassUtil.ABSTRACT_CLASS_PREFIX;
import static startupheroes.checkstyle.util.ClassUtil.getClassName;
import static startupheroes.checkstyle.util.ClassUtil.isEntity;

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
      if (isEntity(ast, abstractEntityAnnotation)) {
         String className = getClassName(ast);
         if (!className.startsWith(ABSTRACT_CLASS_PREFIX)) {
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
