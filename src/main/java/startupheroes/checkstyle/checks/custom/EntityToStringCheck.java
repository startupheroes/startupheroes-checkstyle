package startupheroes.checkstyle.checks.custom;

import antlr.collections.AST;
import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import startupheroes.checkstyle.util.CommonUtil;

import static startupheroes.checkstyle.util.CommonUtil.getClassMethods;

/**
 * @author ozlem.ulag
 */
public class EntityToStringCheck extends AbstractCheck {

   /**
    * A key is pointing to the warning message text in "messages.properties"
    * file.
    */
   static final String MSG_KEY = "entityToStringCheckMessage";

   /**
    * set possible annotations to understand that a class is an entity.
    */
   private Set<String> entityAnnotations = new HashSet<>();

   @Override
   public int[] getDefaultTokens() {
      return new int[]{TokenTypes.CLASS_DEF};
   }

   @Override
   public void visitToken(DetailAST ast) {
      Boolean isEntity = CommonUtil.isEntity(entityAnnotations, ast);
      if (isEntity) {
         List<DetailAST> methods = getClassMethods(ast);
         Boolean containsToString = methods.stream().anyMatch(EntityToStringCheck::isToStringMethod);
         if (!containsToString) {
            log(ast.getLineNo(), MSG_KEY);
         }
      }
   }

   /**
    * Determines if an AST is a valid toString method implementation.
    *
    * @param ast the AST to check
    * @return true if the {code ast} is a toString method.
    */
   private static Boolean isToStringMethod(DetailAST ast) {
      final DetailAST modifiers = ast.getFirstChild();
      final AST type = ast.findFirstToken(TokenTypes.TYPE);
      final AST methodName = ast.findFirstToken(TokenTypes.IDENT);
      final DetailAST parameters = ast.findFirstToken(TokenTypes.PARAMETERS);

      return type.getFirstChild().getText().equals("String")
             && "toString".equals(methodName.getText())
             && modifiers.branchContains(TokenTypes.LITERAL_PUBLIC)
             && !modifiers.branchContains(TokenTypes.LITERAL_STATIC)
             && parameters.getFirstChild() == null
             && (ast.branchContains(TokenTypes.SLIST)
                 || modifiers.branchContains(TokenTypes.LITERAL_NATIVE));
   }

   public void setEntityAnnotations(String... entityAnnotations) {
      Collections.addAll(this.entityAnnotations, entityAnnotations);
   }

}
