package startupheroes.checkstyle.checks;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import static startupheroes.checkstyle.util.ClassUtil.isEntity;
import static startupheroes.checkstyle.util.ClassUtil.isExtendsAnotherClass;
import static startupheroes.checkstyle.util.VariableUtil.getVariableNames;

/**
 * @author ozlem.ulag
 */
public class EntityMandatoryVariableCheck extends AbstractCheck {

   /**
    * A key is pointing to the warning message text in "messages.properties" file.
    */
   private static final String MSG_KEY = "entityMandatoryVariableCheckMessage";

   /**
    * set entity annotation to understand that a class is an entity.
    */
   private String entityAnnotation;

   private String abstractEntityAnnotation;

   /**
    * set mandatory variables that must exist in each entity.
    */
   private Set<String> mandatoryVariables = new HashSet<>();

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
      if ((isEntity(ast, entityAnnotation) || isEntity(ast, abstractEntityAnnotation)) && !isExtendsAnotherClass(ast)) {
         List<String> variableNames = getVariableNames(ast);
         mandatoryVariables.stream()
                           .filter(mandatoryVariable -> !variableNames.contains(mandatoryVariable))
                           .forEach(mandatoryVariable -> log(ast.getLineNo(), MSG_KEY, mandatoryVariable));
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

   public void setMandatoryVariables(String... mandatoryVariables) {
      Collections.addAll(this.mandatoryVariables, mandatoryVariables);
   }

}
