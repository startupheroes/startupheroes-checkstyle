package startupheroes.checkstyle.checks.custom;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import java.util.List;
import startupheroes.checkstyle.util.MethodUtil;

import static startupheroes.checkstyle.util.ClassUtil.isEntity;
import static startupheroes.checkstyle.util.MethodUtil.getMethods;

/**
 * @author ozlem.ulag
 */
public class EntityEqualsHashCodeCheck extends AbstractCheck {

   /**
    * A key is pointing to the warning message text in "messages.properties" file.
    */
   private static final String MSG_KEY = "entityEqualsHashCodeCheckMessage";

   /**
    * set entity annotation to understand that a class is an entity.
    */
   private String entityAnnotation;

   @Override
   public int[] getDefaultTokens() {
      return new int[]{TokenTypes.CLASS_DEF};
   }

   @Override
   public void visitToken(DetailAST ast) {
      if (isEntity(ast, entityAnnotation)) {
         List<DetailAST> methods = getMethods(ast);
         Boolean hasEquals = methods.stream().anyMatch(MethodUtil::isEqualsMethod);
         Boolean hasHashCode = methods.stream().anyMatch(MethodUtil::isHashCodeMethod);
         if (!hasEquals || !hasHashCode) {
            log(ast.getLineNo(), MSG_KEY);
         }
      }
   }

   public void setEntityAnnotation(String entityAnnotation) {
      this.entityAnnotation = entityAnnotation;
   }

}
