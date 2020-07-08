package es.startuphero.checkstyle.util;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.Scope;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import com.puppycrawl.tools.checkstyle.utils.ScopeUtil;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

/**
 * @author ozlem.ulag
 */
public final class VariableUtils {

  private VariableUtils() {
  }

  /**
   * @param classAst : required to be CLASS_DEF type
   * @return variable asts
   */
  public static List<DetailAST> getVariables(DetailAST classAst) {
    DetailAST objBlock = classAst.findFirstToken(TokenTypes.OBJBLOCK);
    return CommonUtils.getChildsByType(objBlock, TokenTypes.VARIABLE_DEF);
  }

  public static List<DetailAST> getStaticVariables(DetailAST classAst) {
    DetailAST objBlock = classAst.findFirstToken(TokenTypes.OBJBLOCK);
    return CommonUtils.getChildsByType(objBlock, TokenTypes.VARIABLE_DEF).stream()
                      .filter(CommonUtils::isStatic)
                      .collect(toList());
  }

  public static List<DetailAST> getNonStaticVariables(DetailAST classAst) {
    DetailAST objBlock = classAst.findFirstToken(TokenTypes.OBJBLOCK);
    return CommonUtils.getChildsByType(objBlock, TokenTypes.VARIABLE_DEF).stream()
                      .filter(ast -> !CommonUtils.isStatic(ast))
                      .collect(toList());
  }

  /**
   * @param classAst : required to be CLASS_DEF type
   * @param scope : modifier of returned variables
   * @return variable asts that in given scope
   */
  public static List<DetailAST> getVariables(DetailAST classAst, Scope scope) {
    return getVariables(classAst).stream()
                                 .filter(variableAst -> scope.equals(getScopeOf(variableAst)))
                                 .collect(toList());
  }

  public static Scope getScopeOf(DetailAST variableAst) {
    DetailAST variableModifiersNode = variableAst.findFirstToken(TokenTypes.MODIFIERS);
    return ScopeUtil.getScopeFromMods(variableModifiersNode);
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
    return getVariables(classAst).stream()
                                 .map(VariableUtils::getVariableName)
                                 .collect(toList());
  }

  /**
   * @param classAst required to be CLASS_DEF type
   * @return : ordered variable name -> variable AST
   */
  public static Map<String, DetailAST> getVariableNameAstMap(DetailAST classAst) {
    List<DetailAST> variables = getVariables(classAst);
    return variables.stream().collect(toMap(VariableUtils::getVariableName,
                                            Function.identity(), (v1, v2) -> null,
                                            LinkedHashMap::new));
  }

  public static Map<String, DetailAST> getVariableNameAstMap(DetailAST classAst, Boolean isStatic) {
    List<DetailAST> variables = isStatic ? getStaticVariables(classAst) : getNonStaticVariables(classAst);
    return variables.stream().collect(toMap(VariableUtils::getVariableName,
                                            Function.identity(), (v1, v2) -> null,
                                            LinkedHashMap::new));
  }

  public static Map<String, DetailAST> getVariableNameAstMap(DetailAST classAst, Scope scope) {
    List<DetailAST> variables = getVariables(classAst, scope);
    return variables.stream().collect(toMap(VariableUtils::getVariableName,
                                            Function.identity(), (v1, v2) -> null,
                                            LinkedHashMap::new));
  }
}
