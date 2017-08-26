package es.startuphero.checkstyle.checks.naming;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import java.util.Map;

import static es.startuphero.checkstyle.util.ClassUtil.ABSTRACT_CLASS_PREFIX;
import static es.startuphero.checkstyle.util.ClassUtil.getClassName;
import static es.startuphero.checkstyle.util.ClassUtil.isEntity;
import static es.startuphero.checkstyle.util.CommonUtil.getNameWithoutContext;
import static es.startuphero.checkstyle.util.VariableUtil.getVariableNameAstMap;

/**
 * @author ozlem.ulag
 */
public class VariableNameCheck extends AbstractCheck {

  /**
   * A key is pointing to the warning message text in "messages.properties" file.
   */
  private static final String MSG_KEY = "illegal.variable.name";

  /**
   * set entity annotation to understand that a class is an entity.
   */
  private String typeAnnotation;

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
    if (isEntity(ast, typeAnnotation)) {
      String className = getClassName(ast);
      if (className.startsWith(ABSTRACT_CLASS_PREFIX)) {
        className = getNameWithoutContext(className, ABSTRACT_CLASS_PREFIX);
      }
      checkVariablesName(ast, className);
    }
  }

  private void checkVariablesName(DetailAST ast, String className) {
    Map<String, DetailAST> variableNameAstMap = getVariableNameAstMap(ast, false);
    for (String variableName : variableNameAstMap.keySet()) {
      Boolean variableNameInContextOfClassName =
          variableName.toLowerCase().contains(className.toLowerCase());
      String suggestedVariableName = getNameWithoutContext(variableName, className);
      if (variableNameInContextOfClassName && !suggestedVariableName.equals(variableName)) {
        log(variableNameAstMap.get(variableName).getLineNo(), MSG_KEY, suggestedVariableName);
      }
    }
  }

  public void setTypeAnnotation(String typeAnnotation) {
    this.typeAnnotation = typeAnnotation;
  }
}
