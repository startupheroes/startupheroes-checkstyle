package startupheroes.checkstyle.checks;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import java.util.List;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import startupheroes.checkstyle.util.MethodUtil;

import static startupheroes.checkstyle.util.ClassUtil.isEntity;
import static startupheroes.checkstyle.util.MethodUtil.getMethods;
import static startupheroes.checkstyle.util.VariableUtil.getNonStaticVariables;
import static startupheroes.checkstyle.util.VariableUtil.getVariables;

/**
 * @author ozlem.ulag
 */
public class EntityToStringCheck extends AbstractCheck {

   /**
    * A key is pointing to the warning message text in "messages.properties" file.
    */
   private static final String MSG_KEY = "entityToStringCheckMessage";

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
      Assert.isTrue(!StringUtils.isEmpty(entityAnnotation));
      if (isEntity(ast, entityAnnotation)) {
         if (!getNonStaticVariables(ast).isEmpty()) {
            List<DetailAST> methods = getMethods(ast);
            Boolean hasToString = methods.stream().anyMatch(MethodUtil::isToStringMethod);
            if (!hasToString) {
               log(ast.getLineNo(), MSG_KEY);
            }
         }
      }
   }

   public void setEntityAnnotation(String entityAnnotation) {
      this.entityAnnotation = entityAnnotation;
   }

}
