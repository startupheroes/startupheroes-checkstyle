package es.startuphero.checkstyle.checks;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import java.util.Map;
import org.springframework.util.Assert;

import static es.startuphero.checkstyle.util.ClassUtil.ABSTRACT_CLASS_PREFIX;
import static es.startuphero.checkstyle.util.ClassUtil.getClassName;
import static es.startuphero.checkstyle.util.ClassUtil.isEntity;
import static es.startuphero.checkstyle.util.CommonUtil.getNameWithoutContext;
import static es.startuphero.checkstyle.util.VariableUtil.getVariableNameAstMap;
import static org.springframework.util.StringUtils.isEmpty;

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

  private String abstractTypeAnnotation;

  public void setTypeAnnotation(String typeAnnotation) {
    this.typeAnnotation = typeAnnotation;
  }

  public void setAbstractTypeAnnotation(String abstractTypeAnnotation) {
    this.abstractTypeAnnotation = abstractTypeAnnotation;
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
    assertions();
    if (isEntity(ast, typeAnnotation) || isEntity(ast, abstractTypeAnnotation)) {
      String className = getClassName(ast);
      if (className.startsWith(ABSTRACT_CLASS_PREFIX)) {
        className = getNameWithoutContext(className, ABSTRACT_CLASS_PREFIX);
      }
      checkVariablesName(ast, className);
    }
  }

  private void assertions() {
    Assert.isTrue(!isEmpty(typeAnnotation));
    Assert.isTrue(!isEmpty(abstractTypeAnnotation));
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
}
