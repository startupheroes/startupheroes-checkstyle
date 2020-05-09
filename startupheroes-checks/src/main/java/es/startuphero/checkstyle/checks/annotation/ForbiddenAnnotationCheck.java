package es.startuphero.checkstyle.checks.annotation;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import com.puppycrawl.tools.checkstyle.utils.CommonUtil;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static es.startuphero.checkstyle.util.AnnotationUtils.getAnnotation;
import static es.startuphero.checkstyle.util.AnnotationUtils.hasAnnotation;
import static es.startuphero.checkstyle.util.CommonUtil.getSimpleName;
import static java.util.Objects.nonNull;

/**
 * @author ozlem.ulag
 */
public class ForbiddenAnnotationCheck extends AbstractCheck {

  /**
   * A key is pointing to the warning message text in "messages.properties" file.
   */
  private static final String MSG_KEY = "forbidden.annotation";

  /**
   * set forbidden annotations on given tokens.
   */
  private Set<String> forbiddenAnnotations = new HashSet<>();

  @Override
  public int[] getDefaultTokens() {
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
  public int[] getAcceptableTokens() {
    return new int[] {
        TokenTypes.CLASS_DEF,
        TokenTypes.INTERFACE_DEF,
        TokenTypes.ENUM_DEF,
        TokenTypes.METHOD_DEF,
        TokenTypes.CTOR_DEF,
        TokenTypes.VARIABLE_DEF,
        TokenTypes.PARAMETER_DEF,
        TokenTypes.ANNOTATION_DEF,
        TokenTypes.TYPECAST,
        TokenTypes.LITERAL_THROWS,
        TokenTypes.IMPLEMENTS_CLAUSE,
        TokenTypes.TYPE_ARGUMENT,
        TokenTypes.LITERAL_NEW,
        TokenTypes.DOT,
        TokenTypes.ANNOTATION_FIELD_DEF,
        };
  }

  @Override
  public int[] getRequiredTokens() {
    return CommonUtil.EMPTY_INT_ARRAY;
  }

  @Override
  public void visitToken(DetailAST ast) {
    forbiddenAnnotations.stream()
                        .filter(forbiddenAnnotation -> hasAnnotation(ast, forbiddenAnnotation))
                        .forEach(forbiddenAnnotation -> log(getAnnotation(ast, forbiddenAnnotation).getLineNo(),
                                                            MSG_KEY,
                                                            getSimpleName(forbiddenAnnotation)));
  }

  public void setForbiddenAnnotations(String... forbiddenAnnotations) {
    if (nonNull(forbiddenAnnotations)) {
      Collections.addAll(this.forbiddenAnnotations, forbiddenAnnotations);
    }
  }
}
