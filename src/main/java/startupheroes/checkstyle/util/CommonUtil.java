package startupheroes.checkstyle.util;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import com.puppycrawl.tools.checkstyle.utils.AnnotationUtility;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author ozlem.ulag
 */
public class CommonUtil {

   /**
    * @param classAst : required to be CLASS_DEF type
    * @return variable asts
    */
   public static List<DetailAST> getClassVariables(DetailAST classAst) {
      DetailAST objBlock = classAst.findFirstToken(TokenTypes.OBJBLOCK);
      return findByType(objBlock, TokenTypes.VARIABLE_DEF);
   }

   /**
    * @param classAst : required to be CLASS_DEF type
    * @return method asts
    */
   public static List<DetailAST> getClassMethods(DetailAST classAst) {
      DetailAST objBlock = classAst.findFirstToken(TokenTypes.OBJBLOCK);
      return findByType(objBlock, TokenTypes.METHOD_DEF);
   }

   /**
    * @param variableAst : required to be VARIABLE_DEF type
    * @return name of the variable
    */
   public static String getVariableName(DetailAST variableAst) {
      DetailAST identifier = variableAst.findFirstToken(TokenTypes.IDENT);
      return identifier.getText();
   }

   /**
    * @param methodAst : required to be METHOD_DEF type
    * @return name of the method
    */
   public static String getMethodName(DetailAST methodAst) {
      DetailAST type = methodAst.findFirstToken(TokenTypes.TYPE);
      return type.getNextSibling().getText();
   }

   /**
    * finds matching AST's that has type given type from given ast.
    */
   public static List<DetailAST> findByType(DetailAST ast, int type) {
      List<DetailAST> astListByType = new ArrayList<>();
      for (DetailAST child = ast.getFirstChild(); child != null; child = child.getNextSibling()) {
         if (child.getType() == type) {
            astListByType.add(child);
         }
      }
      return astListByType;
   }

   public static Boolean isEntity(Set<String> entityAnnotations, DetailAST ast) {
      return entityAnnotations.stream().anyMatch(entityAnnotation -> AnnotationUtility.containsAnnotation(ast, entityAnnotation));
   }

}
