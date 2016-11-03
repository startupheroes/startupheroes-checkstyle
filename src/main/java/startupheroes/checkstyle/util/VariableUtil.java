package startupheroes.checkstyle.util;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.Scope;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import com.puppycrawl.tools.checkstyle.utils.ScopeUtils;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static startupheroes.checkstyle.util.CommonUtil.getChildsByType;
import static startupheroes.checkstyle.util.CommonUtil.isStatic;

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

   public static List<DetailAST> getStaticVariables(DetailAST classAst) {
      DetailAST objBlock = classAst.findFirstToken(TokenTypes.OBJBLOCK);
      return getChildsByType(objBlock, TokenTypes.VARIABLE_DEF).stream()
                                                               .filter(CommonUtil::isStatic)
                                                               .collect(Collectors.toList());
   }

   public static List<DetailAST> getNonStaticVariables(DetailAST classAst) {
      DetailAST objBlock = classAst.findFirstToken(TokenTypes.OBJBLOCK);
      return getChildsByType(objBlock, TokenTypes.VARIABLE_DEF).stream()
                                                               .filter(ast -> !isStatic(ast))
                                                               .collect(Collectors.toList());
   }

   /**
    * @param classAst : required to be CLASS_DEF type
    * @param scope : modifier of returned variables
    * @return variable asts that in given scope
    */
   public static List<DetailAST> getVariables(DetailAST classAst, Scope scope) {
      return getVariables(classAst).stream()
                                   .filter(variableAst -> scope.equals(getScopeOf(variableAst)))
                                   .collect(Collectors.toList());
   }

   public static Scope getScopeOf(DetailAST variableAst) {
      DetailAST variableModifiersNode = variableAst.findFirstToken(TokenTypes.MODIFIERS);
      return ScopeUtils.getScopeFromMods(variableModifiersNode);
   }

   /**
    * @param variableAst : required to be VARIABLE_DEF type
    * @return name of the variable
    */
   public static String getVariableName(DetailAST variableAst) {
      DetailAST type = variableAst.findFirstToken(TokenTypes.TYPE);
      return type.getNextSibling().getText();
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

   public static Map<String, DetailAST> getVariableNameAstMap(DetailAST classAst, Boolean isStatic) {
      List<DetailAST> variables = isStatic ? getStaticVariables(classAst) : getNonStaticVariables(classAst);
      return variables.stream().collect(Collectors.toMap(VariableUtil::getVariableName,
                                                         Function.identity(),
                                                         (v1, v2) -> null,
                                                         LinkedHashMap::new));
   }

   public static Map<String, DetailAST> getVariableNameAstMap(DetailAST classAst, Scope scope) {
      List<DetailAST> variables = getVariables(classAst, scope);
      return variables.stream().collect(Collectors.toMap(VariableUtil::getVariableName,
                                                         Function.identity(),
                                                         (v1, v2) -> null,
                                                         LinkedHashMap::new));
   }

}
