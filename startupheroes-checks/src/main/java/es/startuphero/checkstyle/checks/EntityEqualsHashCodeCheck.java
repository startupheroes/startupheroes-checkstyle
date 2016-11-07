package es.startuphero.checkstyle.checks;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import es.startuphero.checkstyle.util.AnnotationUtil;
import es.startuphero.checkstyle.util.ClassUtil;
import es.startuphero.checkstyle.util.MethodUtil;
import es.startuphero.checkstyle.util.VariableUtil;
import java.util.List;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

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

   private String abstractEntityAnnotation;

   private String idAnnotation;

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
         Boolean entityHasAnyId = VariableUtil.getNonStaticVariables(ast).stream().anyMatch(variable -> AnnotationUtil.hasAnnotation(variable, idAnnotation));
         if (entityHasAnyId) {
            List<DetailAST> methods = MethodUtil.getMethods(ast);
            Boolean hasEquals = methods.stream().anyMatch(MethodUtil::isEqualsMethod);
            Boolean hasHashCode = methods.stream().anyMatch(MethodUtil::isHashCodeMethod);
            if (!hasEquals || !hasHashCode) {
               log(ast.getLineNo(), MSG_KEY);
            }
         }
      }
   }

   private void assertions() {
      Assert.isTrue(!StringUtils.isEmpty(entityAnnotation));
      Assert.isTrue(!StringUtils.isEmpty(abstractEntityAnnotation));
      Assert.isTrue(!StringUtils.isEmpty(idAnnotation));
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

}
