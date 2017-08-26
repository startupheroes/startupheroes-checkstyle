package es.startuphero.checkstyle.checks.modifier;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

import static es.startuphero.checkstyle.util.ClassUtil.isAbstract;
import static es.startuphero.checkstyle.util.ClassUtil.isEntity;

/**
 * @author ozlem.ulag
 */
public class MissingAbstractModifierCheck extends AbstractCheck {

  /**
   * A key is pointing to the warning message text in "messages.properties" file.
   */
  private static final String MSG_KEY = "missing.abstract.modifier";

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
    if (isEntity(ast, abstractTypeAnnotation) && !isAbstract(ast)) {
      log(ast.getLineNo(), MSG_KEY, abstractTypeAnnotation);
    }
  }

  public void setAbstractTypeAnnotation(String abstractTypeAnnotation) {
    this.abstractTypeAnnotation = abstractTypeAnnotation;
  }
}
