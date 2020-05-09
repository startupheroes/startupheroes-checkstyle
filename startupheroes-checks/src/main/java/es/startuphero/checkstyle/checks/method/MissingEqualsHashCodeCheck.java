package es.startuphero.checkstyle.checks.method;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import es.startuphero.checkstyle.util.MethodUtils;
import java.util.List;

import static es.startuphero.checkstyle.util.AnnotationUtils.hasAnnotation;
import static es.startuphero.checkstyle.util.ClassUtils.isEntity;
import static es.startuphero.checkstyle.util.MethodUtils.getMethods;
import static es.startuphero.checkstyle.util.VariableUtils.getNonStaticVariables;

/**
 * @author ozlem.ulag
 */
public class MissingEqualsHashCodeCheck extends AbstractCheck {

  /**
   * A key is pointing to the warning message text in "messages.properties" file.
   */
  private static final String MSG_KEY = "missing.equals.hashcode";

  /**
   * set type annotation to understand that a class is an entity.
   */
  private String typeAnnotation;

  /**
   * set abstract type annotation to understand that a class is abstract.
   */
  private String abstractTypeAnnotation;

  private String idAnnotation;

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
      Boolean entityHasAnyId = getNonStaticVariables(ast).stream()
                                                         .anyMatch(variable -> hasAnnotation(variable, idAnnotation));
      if (entityHasAnyId) {
        List<DetailAST> methods = getMethods(ast);
        Boolean hasEquals = methods.stream().anyMatch(MethodUtils::isEqualsMethod);
        Boolean hasHashCode = methods.stream().anyMatch(MethodUtils::isHashCodeMethod);
        if (!hasEquals || !hasHashCode) {
          log(ast.getLineNo(), MSG_KEY);
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

  public void setIdAnnotation(String idAnnotation) {
    this.idAnnotation = idAnnotation;
  }
}
