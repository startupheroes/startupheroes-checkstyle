package startupheroes.checkstyle.util;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static startupheroes.checkstyle.util.CommonUtil.getChildsByType;

/**
 * @author ozlem.ulag
 */
public final class VariableUtil {

   private VariableUtil() {
   }

   /**
    * @param classAst : required to be CLASS_DEF type
    * @return variable asts
    */
   public static List<DetailAST> getVariables(DetailAST classAst) {
      DetailAST objBlock = classAst.findFirstToken(TokenTypes.OBJBLOCK);
      return getChildsByType(objBlock, TokenTypes.VARIABLE_DEF);
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
      return getVariables(classAst).stream().map(VariableUtil::getVariableName).collect(Collectors.toList());
   }

   /**
    * @param classAst required to be CLASS_DEF type
    * @return : ordered variable name -> variable AST
    */
   public static Map<String, DetailAST> getVariableNameAstMap(DetailAST classAst) {
      List<DetailAST> variables = getVariables(classAst);
      return variables.stream().collect(Collectors.toMap(VariableUtil::getVariableName,
                                                         Function.identity(),
                                                         (v1, v2) -> null,
                                                         LinkedHashMap::new));
   }

}
