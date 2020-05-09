package es.startuphero.checkstyle.checks.variable;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static es.startuphero.checkstyle.util.ClassUtils.isEntity;
import static es.startuphero.checkstyle.util.ClassUtils.isExtendsAnotherClass;
import static es.startuphero.checkstyle.util.VariableUtils.getVariableNames;

/**
 * @author ozlem.ulag
 */
public class MissingVariableCheck extends AbstractCheck {

  /**
   * A key is pointing to the warning message text in "messages.properties" file.
   */
  private static final String MSG_KEY = "missing.variable";

  /**
   * set type annotation to understand that a class is an entity.
   */
  private String typeAnnotation;

  /**
   * set abstract type annotation to understand that a class is abstract.
   */
  private String abstractTypeAnnotation;

  /**
   * set mandatory variables that must exist in each entity.
   */
  private Set<String> mandatoryVariables = new HashSet<>();

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
    if ((isEntity(ast, typeAnnotation) ||
         isEntity(ast, abstractTypeAnnotation)) &&
        !isExtendsAnotherClass(ast)) {
      List<String> variableNames = getVariableNames(ast);
      mandatoryVariables.stream()
                        .filter(mandatoryVariable -> !variableNames.contains(mandatoryVariable))
                        .forEach(mandatoryVariable -> log(ast.getLineNo(), MSG_KEY, mandatoryVariable));
    }
  }

  public void setTypeAnnotation(String typeAnnotation) {
    this.typeAnnotation = typeAnnotation;
  }

  public void setAbstractTypeAnnotation(String abstractTypeAnnotation) {
    this.abstractTypeAnnotation = abstractTypeAnnotation;
  }

  public void setMandatoryVariables(String... mandatoryVariables) {
    Collections.addAll(this.mandatoryVariables, mandatoryVariables);
  }
}
