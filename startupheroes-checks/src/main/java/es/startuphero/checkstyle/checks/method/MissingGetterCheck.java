package es.startuphero.checkstyle.checks.method;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import es.startuphero.checkstyle.util.MethodUtils;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static es.startuphero.checkstyle.util.ClassUtils.isEntity;
import static es.startuphero.checkstyle.util.MethodUtils.getGetters;
import static es.startuphero.checkstyle.util.MethodUtils.getMethodName;
import static es.startuphero.checkstyle.util.VariableUtils.getVariableNameAstMap;

/**
 * @author ozlem.ulag
 */
public class MissingGetterCheck extends AbstractCheck {

  /**
   * A key is pointing to the warning message text in "messages.properties" file.
   */
  private static final String MSG_KEY = "missing.getter";

  /**
   * set type annotation to understand that a class is an entity.
   */
  private String typeAnnotation;

  /**
   * set abstract type annotation to understand that a class is abstract.
   */
  private String abstractTypeAnnotation;

  @Override
  public int[] getDefaultTokens() {
    return getAcceptableTokens();
  }

  @Override
  public int[] getAcceptableTokens() {
    return new int[] {TokenTypes.CLASS_DEF};
  }

  @Override
  public int[] getRequiredTokens() {
    return getAcceptableTokens();
  }

  @Override
  public void visitToken(DetailAST ast) {
    if (isEntity(ast, typeAnnotation) || isEntity(ast, abstractTypeAnnotation)) {
      Map<String, DetailAST> variableNameAstMap = getVariableNameAstMap(ast, false);
      List<String> getterVariableNames = getGetters(ast).stream()
                                                        .map(getter -> getMethodName(getter).split(
                                                            MethodUtils.GETTER_PREFIX_REGEX)[1])
                                                        .collect(Collectors.toList());

      for (String variableName : variableNameAstMap.keySet()) {
        String nameInGettersAndSetters =
            variableName.substring(0, 1).toUpperCase() + variableName.substring(1);
        if (!getterVariableNames.contains(nameInGettersAndSetters)) {
          log(variableNameAstMap.get(variableName).getLineNo(), MSG_KEY, variableName);
        }
      }
    }
  }

  public void setTypeAnnotation(String typeAnnotation) {
    this.typeAnnotation = typeAnnotation;
  }

  public void setAbstractTypeAnnotation(String abstractTypeAnnotation) {
    this.abstractTypeAnnotation = abstractTypeAnnotation;
  }
}
