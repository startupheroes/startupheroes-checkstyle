package es.startuphero.checkstyle.checks.method;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import es.startuphero.checkstyle.util.MethodUtil;
import es.startuphero.checkstyle.util.VariableUtil;
import java.util.List;

import static es.startuphero.checkstyle.util.ClassUtil.isEntity;
import static es.startuphero.checkstyle.util.MethodUtil.getMethods;
import static es.startuphero.checkstyle.util.StringUtil.isEmpty;

/**
 * @author ozlem.ulag
 */
public class MissingToStringCheck extends AbstractCheck {

  /**
   * A key is pointing to the warning message text in "messages.properties" file.
   */
  private static final String MSG_KEY = "missing.to.string";

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
      if (!VariableUtil.getNonStaticVariables(ast).isEmpty()) {
        List<DetailAST> methods = getMethods(ast);
        Boolean hasToString = methods.stream().anyMatch(MethodUtil::isToStringMethod);
        if (!hasToString) {
          log(ast.getLineNo(), MSG_KEY);
        }
      }
    }
  }

  private void assertions() {
    assert !isEmpty(typeAnnotation);
    assert !isEmpty(abstractTypeAnnotation);
  }
}
