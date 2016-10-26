package startupheroes.checkstyle.util;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import com.puppycrawl.tools.checkstyle.utils.AnnotationUtility;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author ozlem.ulag
 */
public class CommonUtil {

   /**
    * @param entityAnnotations : possible entity annotations to undestand that a class is an entity.
    * @param ast: required to be CLASS_DEF type
    * @return : true if class is an entity, otherwise false
    */
   public static Boolean isEntity(Set<String> entityAnnotations, DetailAST ast) {
      return entityAnnotations.stream().anyMatch(entityAnnotation -> AnnotationUtility.containsAnnotation(ast, entityAnnotation));
   }

   /**
    * @param classAst : required to be CLASS_DEF type
    * @return variable asts
    */
   public static List<DetailAST> getVariables(DetailAST classAst) {
      DetailAST objBlock = classAst.findFirstToken(TokenTypes.OBJBLOCK);
      return findByType(objBlock, TokenTypes.VARIABLE_DEF);
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
    * @param classAst : required to be CLASS_DEF type
    * @return variable names
    */
   public static List<String> getVariableNames(DetailAST classAst) {
      return getVariables(classAst).stream().map(CommonUtil::getVariableName).collect(Collectors.toList());
   }

   /**
    * @param classAst required to be CLASS_DEF type
    * @param ordered order map as declaration order in class or not.
    * @return : variable name -> variable AST
    */
   public static Map<String, DetailAST> getVariableNameAstMap(DetailAST classAst, Boolean ordered) {
      List<DetailAST> variables = getVariables(classAst);

      return ordered ?
          variables.stream().collect(Collectors.toMap(CommonUtil::getVariableName,
                                                      Function.identity(),
                                                      (v1, v2) -> null,
                                                      LinkedHashMap::new)) :
          variables.stream()
                   .collect(Collectors.toMap(CommonUtil::getVariableName, Function.identity()));
   }

   /**
    * @param classAst : required to be CLASS_DEF type
    * @return method asts
    */
   public static List<DetailAST> getMethods(DetailAST classAst) {
      DetailAST objBlock = classAst.findFirstToken(TokenTypes.OBJBLOCK);
      return findByType(objBlock, TokenTypes.METHOD_DEF);
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

}
