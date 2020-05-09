package es.startuphero.checkstyle.checks.annotation;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import es.startuphero.checkstyle.util.CommonUtils;
import java.util.HashMap;
import java.util.Map;

import static es.startuphero.checkstyle.util.AnnotationUtils.hasAnnotation;
import static es.startuphero.checkstyle.util.CommonUtils.getSimpleName;

/**
 * @author ozlem.ulag
 */
public class RedundantMultipleAnnotationCheck extends AbstractCheck {

  /**
   * A key is pointing to the warning message text in "messages.properties" file.
   */
  private static final String MSG_KEY = "redundant.multiple.annotation";

  private Map<String, String> redundantAnnotationPairs = new HashMap<>();

  @Override
  public int[] getDefaultTokens() {
    return getAcceptableTokens();
  }

  @Override
  public int[] getAcceptableTokens() {
    return new int[] {
        TokenTypes.CLASS_DEF,
        TokenTypes.INTERFACE_DEF,
        TokenTypes.ENUM_DEF,
        TokenTypes.METHOD_DEF,
        TokenTypes.CTOR_DEF,
        TokenTypes.VARIABLE_DEF,
        };
  }

  @Override
  public int[] getRequiredTokens() {
    return getAcceptableTokens();
  }

  @Override
  public void visitToken(DetailAST ast) {
    for (String annotation1 : redundantAnnotationPairs.keySet()) {
      String annotation2 = redundantAnnotationPairs.get(annotation1);
      if (hasAnnotation(ast, annotation1) && hasAnnotation(ast, annotation2)) {
        log(ast.getLineNo(), MSG_KEY, getSimpleName(annotation1), getSimpleName(annotation2));
      }
    }
  }

  public void setRedundantAnnotationPairs(String property) {
    this.redundantAnnotationPairs = CommonUtils.splitProperty(property);
  }
}
