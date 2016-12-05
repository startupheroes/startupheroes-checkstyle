package es.startuphero.checkstyle.checks.coding;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.Scope;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static es.startuphero.checkstyle.util.VariableUtil.getScopeOf;
import static es.startuphero.checkstyle.util.VariableUtil.getVariableNameAstMap;

/**
 * @author ozlem.ulag
 */
public class VariableDeclarationOrderCheck extends AbstractCheck {

  /**
   * A key is pointing to the warning message text in "messages.properties" file.
   */
  private static final String MSG_KEY = "illegal.variable.declaration.order";

  /**
   * set variable name to check declaration order.
   */
  private String variableName;

  /**
   * set declaration order of given variable.
   */
  private Integer declarationOrder;

  public void setVariableName(String variableName) {
    this.variableName = variableName;
  }

  public void setDeclarationOrder(Integer declarationOrder) {
    this.declarationOrder = declarationOrder;
  }

  @Override
  public int[] getDefaultTokens() {
    return getAcceptableTokens();
  }

  @Override
  public int[] getAcceptableTokens() {
    return new int[] {TokenTypes.CLASS_DEF,
                      TokenTypes.INTERFACE_DEF,
                      TokenTypes.ENUM_DEF,
                      TokenTypes.ANNOTATION_DEF};
  }

  @Override
  public int[] getRequiredTokens() {
    return getAcceptableTokens();
  }

  @Override
  public void visitToken(DetailAST ast) {
    Map<String, DetailAST> variableNameAstMap = getVariableNameAstMap(ast);
    if (variableNameAstMap.containsKey(variableName)) {
      DetailAST variableAst = variableNameAstMap.get(variableName);
      Scope scopeOfVariable = getScopeOf(variableAst);
      variableNameAstMap = getVariableNameAstMap(ast, scopeOfVariable);
      if (isVariableNotInCorrectOrder(variableNameAstMap)) {
        log(variableAst.getLineNo(), MSG_KEY, variableName, declarationOrder);
      }
    }
  }

  private Boolean isVariableNotInCorrectOrder(Map<String, DetailAST> variableNameAstMap) {
    List<String> variableNames = variableNameAstMap.keySet().stream().collect(Collectors.toList());
    return variableNames.indexOf(variableName) != declarationOrder - 1;
  }
}
