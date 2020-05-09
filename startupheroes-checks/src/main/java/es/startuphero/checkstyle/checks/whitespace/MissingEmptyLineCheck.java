package es.startuphero.checkstyle.checks.whitespace;

import com.google.common.base.CharMatcher;
import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

/**
 * @author ozlem.ulag
 */
public class MissingEmptyLineCheck extends AbstractCheck {

  /**
   * A key is pointing to the warning message text in "messages.properties" file.
   */
  private static final String MSG_KEY = "missing.empty.line";

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
    DetailAST objBlockNode = ast.findFirstToken(TokenTypes.OBJBLOCK);
    DetailAST rightCurlyNode = objBlockNode.findFirstToken(TokenTypes.RCURLY);
    int lineNoBeforeLastLine = rightCurlyNode.getLineNo() - 1;
    String lineBeforeLastLine = getLine(lineNoBeforeLastLine - 1);
    if (!CharMatcher.whitespace().matchesAllOf(lineBeforeLastLine)) {
      log(lineNoBeforeLastLine, MSG_KEY);
    }
  }
}
