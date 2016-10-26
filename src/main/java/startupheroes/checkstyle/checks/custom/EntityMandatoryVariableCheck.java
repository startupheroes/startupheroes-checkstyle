package startupheroes.checkstyle.checks.custom;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import startupheroes.checkstyle.util.CommonUtil;

import static startupheroes.checkstyle.util.CommonUtil.getVariableNames;

/**
 * @author ozlem.ulag
 */
public class EntityMandatoryVariableCheck extends AbstractCheck {

   /**
    * A key is pointing to the warning message text in "messages.properties"
    * file.
    */
   static final String MSG_KEY = "entityMandatoryVariableCheckMessage";

   /**
    * set possible annotations to understand that a class is an entity.
    */
   private Set<String> entityAnnotations = new HashSet<>();

   /**
    * set mandotary variables that must exist in each entity.
    */
   private Set<String> mandatoryVariables = new HashSet<>();

   @Override
   public int[] getDefaultTokens() {
      return new int[]{TokenTypes.CLASS_DEF};
   }

   @Override
   public void visitToken(DetailAST ast) {
      if (CommonUtil.isEntity(entityAnnotations, ast)) {
         List<String> variableNames = getVariableNames(ast);
         mandatoryVariables.stream()
                           .filter(mandatoryVariable -> !variableNames.contains(mandatoryVariable))
                           .forEach(mandatoryVariable -> log(ast.getLineNo(), MSG_KEY, mandatoryVariable));
      }
   }

   public void setEntityAnnotations(String... entityAnnotations) {
      Collections.addAll(this.entityAnnotations, entityAnnotations);
   }

   public void setMandatoryVariables(String... mandatoryVariables) {
      Collections.addAll(this.mandatoryVariables, mandatoryVariables);
   }

}
