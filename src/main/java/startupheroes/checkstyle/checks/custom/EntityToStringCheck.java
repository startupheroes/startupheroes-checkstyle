package startupheroes.checkstyle.checks.custom;

import antlr.collections.AST;
import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import com.puppycrawl.tools.checkstyle.utils.AnnotationUtility;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
      Boolean isEntity = isEntity(ast);
      if (isEntity) {
         List<DetailAST> methods = findByType(ast.findFirstToken(TokenTypes.OBJBLOCK), TokenTypes.METHOD_DEF);
         Boolean containsToString = methods.stream().anyMatch(EntityToStringCheck::isToStringMethod);
         if (!containsToString) {
            log(ast.getLineNo(), MSG_KEY);
         }
      }
   }

   private Boolean isEntity(DetailAST ast) {
      return entityAnnotations.stream().anyMatch(entityAnnotation -> AnnotationUtility.containsAnnotation(ast, entityAnnotation));
   }

   private static List<DetailAST> findByType(DetailAST ast, int type) {
      List<DetailAST> astListByType = new ArrayList<>();
      for (DetailAST child = ast.getFirstChild(); child != null; child = child.getNextSibling()) {
         if (child.getType() == type) {
            astListByType.add(child);
         }
      }
      return astListByType;
   }

   /**
    * Determines if an AST is a valid HashCode method implementation.
    *
    * @param ast the AST to check
    * @return true if the {code ast} is a HashCode method.
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
