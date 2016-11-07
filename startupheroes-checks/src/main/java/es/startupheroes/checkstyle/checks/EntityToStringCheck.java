package es.startupheroes.checkstyle.checks;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import es.startupheroes.checkstyle.util.ClassUtil;
import java.util.List;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import es.startupheroes.checkstyle.util.MethodUtil;

import static es.startupheroes.checkstyle.util.MethodUtil.getMethods;
import static es.startupheroes.checkstyle.util.VariableUtil.getNonStaticVariables;

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
      if (ClassUtil.isEntity(ast, entityAnnotation) || ClassUtil.isEntity(ast, abstractEntityAnnotation)) {
         if (!getNonStaticVariables(ast).isEmpty()) {
            List<DetailAST> methods = getMethods(ast);
            Boolean hasToString = methods.stream().anyMatch(MethodUtil::isToStringMethod);
            if (!hasToString) {
               log(ast.getLineNo(), MSG_KEY);
            }
         }
      }
   }

   private void assertions() {
      Assert.isTrue(!StringUtils.isEmpty(entityAnnotation));
      Assert.isTrue(!StringUtils.isEmpty(abstractEntityAnnotation));
   }

   public void setEntityAnnotation(String entityAnnotation) {
      this.entityAnnotation = entityAnnotation;
   }

   public void setAbstractEntityAnnotation(String abstractEntityAnnotation) {
      this.abstractEntityAnnotation = abstractEntityAnnotation;
   }

}
