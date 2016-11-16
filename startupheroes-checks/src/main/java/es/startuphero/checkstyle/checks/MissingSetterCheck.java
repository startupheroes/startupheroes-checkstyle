package es.startuphero.checkstyle.checks;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import es.startuphero.checkstyle.util.MethodUtil;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import static es.startuphero.checkstyle.util.ClassUtil.isEntity;
import static es.startuphero.checkstyle.util.MethodUtil.getMethodName;
import static es.startuphero.checkstyle.util.MethodUtil.getSetters;
import static es.startuphero.checkstyle.util.VariableUtil.getVariableNameAstMap;

/**
 * @author ozlem.ulag
 */
public class MissingSetterCheck extends AbstractCheck {

  /**
   * A key is pointing to the warning message text in "messages.properties" file.
   */
  private static final String MSG_KEY = "missing.setter";

  /**
   * set type annotation to understand that a class is an entity.
   */
  private String typeAnnotation;

  /**
   * set abstract type annotation to understand that a class is abstract.
   */
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
    return new int[] {TokenTypes.CLASS_DEF};
  }

  @Override
  public int[] getRequiredTokens() {
    return getAcceptableTokens();
  }

  @Override
  public void visitToken(DetailAST ast) {
    assertions();
    if (isEntity(ast, typeAnnotation) || isEntity(ast, abstractTypeAnnotation)) {
      Map<String, DetailAST> variableNameAstMap = getVariableNameAstMap(ast, false);
      List<String> setterVariableNames = getSetters(ast).stream()
          .map(setter -> getMethodName(setter).split(MethodUtil.SETTER_PREFIX_REGEX)[1])
          .collect(Collectors.toList());

      for (String variableName : variableNameAstMap.keySet()) {
        String nameInGettersAndSetters =
            variableName.substring(0, 1).toUpperCase() + variableName.substring(1);
        if (!setterVariableNames.contains(nameInGettersAndSetters)) {
          log(variableNameAstMap.get(variableName).getLineNo(), MSG_KEY, variableName);
        }
      }
    }
  }

  private void assertions() {
    Assert.isTrue(!StringUtils.isEmpty(typeAnnotation));
    Assert.isTrue(!StringUtils.isEmpty(abstractTypeAnnotation));
  }
}
