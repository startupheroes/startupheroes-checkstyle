package es.startuphero.checkstyle.util;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import com.puppycrawl.tools.checkstyle.utils.AnnotationUtil;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

/**
 * @author ozlem.ulag
 */
public final class AnnotationUtils {

  public static final String OVERRIDE_ANNOTATION_NAME_BY_PACKAGE = "java.lang.Override";

  private AnnotationUtils() {
  }

  /**
   * @param fullAnnotation : Full qualifier of annotation like java.lang.Object
   * @return : true if ast has any possible annotation, otherwise false
   */
  public static Boolean hasAnnotation(DetailAST ast, String fullAnnotation) {
    return CommonUtil.getSimpleAndFullNames(fullAnnotation).stream()
                     .anyMatch(annotation -> AnnotationUtil.containsAnnotation(ast, annotation));
  }

  public static DetailAST getAnnotation(DetailAST ast, String fullAnnotation) {
    DetailAST simpleAnnotationAst =
        AnnotationUtil.getAnnotation(ast, CommonUtil.getSimpleName(fullAnnotation));
    return nonNull(simpleAnnotationAst) ? simpleAnnotationAst
        : AnnotationUtil.getAnnotation(ast, fullAnnotation);
  }

  public static List<String> getKeys(DetailAST annotationAst) {
    List<DetailAST> keyValuePairAstList =
        CommonUtil.getChildsByType(annotationAst, TokenTypes.ANNOTATION_MEMBER_VALUE_PAIR);
    return keyValuePairAstList.stream()
                              .map(keyValuePairAst -> keyValuePairAst.getFirstChild().getText())
                              .collect(Collectors.toList());
  }

  /**
   * key -> ANNOTATION_MEMBER_VALUE_PAIR type ast
   */
  public static Map<String, DetailAST> getKeyValueAstMap(DetailAST annotationAst) {
    List<DetailAST> keyValuePairAstList =
        CommonUtil.getChildsByType(annotationAst, TokenTypes.ANNOTATION_MEMBER_VALUE_PAIR);
    return keyValuePairAstList.stream()
                              .collect(Collectors.toMap(keyValuePairAst -> keyValuePairAst.getFirstChild().getText(),
                                                        Function.identity(),
                                                        (v1, v2) -> null,
                                                        LinkedHashMap::new));
  }

  /**
   * @param annotationKeyValueAst : ANNOTATION_MEMBER_VALUE_PAIR type ast
   * @return value of key
   */
  public static Optional<String> getValueAsString(DetailAST annotationKeyValueAst) {
    Optional<String> result = Optional.empty();
    DetailAST annotationValueNode = getAnnotationValueNode(annotationKeyValueAst);
    DetailAST literalValueAst = annotationValueNode.getFirstChild();
    if (nonNull(literalValueAst)) {
      result = Optional.of(literalValueAst.getText().replaceAll("\"", ""));
    }
    return result;
  }

  public static List<String> getValueAsStringList(DetailAST annotationKeyValueAst) {
    DetailAST annotationValueNode = getAnnotationValueNode(annotationKeyValueAst);
    List<DetailAST> exprNodes = annotationValueNode.getType() == TokenTypes.EXPR ?
        Collections.singletonList(annotationValueNode) :
        CommonUtil.getChildsByType(annotationValueNode, TokenTypes.EXPR);
    return exprNodes.stream()
                    .map(exprNode -> exprNode.findFirstToken(TokenTypes.STRING_LITERAL)
                                             .getText()
                                             .replaceAll("\"", ""))
                    .collect(Collectors.toList());
  }

  public static List<DetailAST> getValueAsAnnotations(DetailAST annotationKeyValueAst) {
    // can be in curly bracket or only one annotation!
    DetailAST annotationValueNode = getAnnotationValueNode(annotationKeyValueAst);
    return annotationValueNode.getType() == TokenTypes.ANNOTATION ?
        Collections.singletonList(annotationValueNode) :
        CommonUtil.getChildsByType(annotationValueNode, TokenTypes.ANNOTATION);
  }

  private static DetailAST getAnnotationValueNode(DetailAST annotationKeyValueAst) {
    return annotationKeyValueAst.findFirstToken(TokenTypes.ASSIGN).getNextSibling();
  }

  public static Object getDefaultValue(String fullAnnotationName, String key) {
    Object defaultValue = null;
    try {
      Class<?> annotationClass = Class.forName(fullAnnotationName);
      defaultValue = getDefaultValue(annotationClass, key);
    } catch (ClassNotFoundException ignored) {
    }
    return defaultValue;
  }

  public static Object getDefaultValue(Class<?> annotationClass, String key) {
    Object defaultValue = null;
    try {
      Method method = annotationClass.getDeclaredMethod(key);
      defaultValue = method.getDefaultValue();
    } catch (NoSuchMethodException ignored) {
    }
    return defaultValue;
  }

  /**
   * key -> default value
   */
  public static Map<String, Object> getKeyDefaultValueMap(String fullAnnotationName,
                                                          Collection<String> keys) {
    Map<String, Object> keyDefaultValueMap = new LinkedHashMap<>();
    try {
      Class<?> annotationClass = Class.forName(fullAnnotationName);
      keys.forEach(key -> keyDefaultValueMap.put(key, getDefaultValue(annotationClass, key)));
    } catch (ClassNotFoundException ignored) {
    }
    return keyDefaultValueMap;
  }
}
