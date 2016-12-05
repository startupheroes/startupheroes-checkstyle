package es.startuphero.checkstyle.checks.annotation;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static es.startuphero.checkstyle.util.AnnotationUtil.getKeys;
import static es.startuphero.checkstyle.util.CommonUtil.getSimpleName;
import static es.startuphero.checkstyle.util.CommonUtil.getSplitterOnComma;
import static es.startuphero.checkstyle.util.CommonUtil.splitProperty;
import static java.util.Objects.nonNull;

/**
 * @author ozlem.ulag
 */
public class ForbiddenAnnotationKeyCheck extends AbstractCheck {

  /**
   * A key is pointing to the warning message text in "messages.properties" file.
   */
  private static final String MSG_KEY = "forbidden.annotation.key";

  /**
   * set forbidden keys that used inside annotation.
   *
   * annotation -> forbidden keys
   */
  private Map<String, Set<String>> annotationForbiddenKeysMap = new HashMap<>();

  public void setAnnotationForbiddenKeysMap(String property) {
    if (nonNull(property)) {
      Map<String, String> annotationKeysMap = splitProperty(property);
      for (String annotation : annotationKeysMap.keySet()) {
        Iterable<String> iterableKeys =
            getSplitterOnComma().split(annotationKeysMap.get(annotation));
        for (String forbiddenKey : iterableKeys) {
          Set<String> forbiddenKeys =
              annotationForbiddenKeysMap.getOrDefault(annotation, new HashSet<>());
          forbiddenKeys.add(forbiddenKey);
          annotationForbiddenKeysMap.put(annotation, forbiddenKeys);
        }
      }
    }
  }

  @Override
  public int[] getDefaultTokens() {
    return getAcceptableTokens();
  }

  @Override
  public int[] getAcceptableTokens() {
    return new int[] {TokenTypes.ANNOTATION};
  }

  @Override
  public int[] getRequiredTokens() {
    return getAcceptableTokens();
  }

  @Override
  public void visitToken(DetailAST ast) {
    Set<String> checkedAnnotations = annotationForbiddenKeysMap.keySet();
    String annotationSimpleName = getSimpleName(ast);
    checkedAnnotations.stream()
                      .filter(checkedAnnotation -> getSimpleName(checkedAnnotation).equals(annotationSimpleName))
                      .forEach(checkedAnnotation -> {
                        List<String> annotationKeys = getKeys(ast);
                        Set<String> forbiddenKeys = annotationForbiddenKeysMap.get(checkedAnnotation);
                        for (String forbiddenKey : forbiddenKeys) {
                          if (annotationKeys.contains(forbiddenKey)) {
                            log(ast.getLineNo(), MSG_KEY, forbiddenKey, annotationSimpleName);
                          }
                        }
                      });
  }
}
