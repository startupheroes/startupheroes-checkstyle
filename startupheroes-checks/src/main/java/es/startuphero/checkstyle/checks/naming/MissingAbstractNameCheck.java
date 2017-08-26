package es.startuphero.checkstyle.checks.naming;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

import static es.startuphero.checkstyle.util.ClassUtil.ABSTRACT_CLASS_PREFIX;
import static es.startuphero.checkstyle.util.ClassUtil.getClassName;
import static es.startuphero.checkstyle.util.ClassUtil.isEntity;

/**
 * @author ozlem.ulag
 */
public class MissingAbstractNameCheck extends AbstractCheck {

  /**
   * A key is pointing to the warning message text in "messages.properties" file.
   */
  private static final String MSG_KEY = "missing.abstract.name";

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
    if (isEntity(ast, abstractTypeAnnotation)) {
      String className = getClassName(ast);
      if (!className.startsWith(ABSTRACT_CLASS_PREFIX)) {
        log(ast.getLineNo(), MSG_KEY);
      }
    }
  }

  public void setAbstractTypeAnnotation(String abstractTypeAnnotation) {
    this.abstractTypeAnnotation = abstractTypeAnnotation;
  }
}
