package startupheroes.checkstyle.util;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import com.puppycrawl.tools.checkstyle.utils.AnnotationUtility;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;
import static startupheroes.checkstyle.util.CommonUtil.findByType;
import static startupheroes.checkstyle.util.CommonUtil.getListOfSimpleAndFullName;
import static startupheroes.checkstyle.util.CommonUtil.getSimpleName;

/**
 * @author ozlem.ulag
 */
public final class AnnotationUtil {

   private AnnotationUtil() {
   }

   /**
    * @param fullAnnotation : Full qualifier of annotation like java.lang.Object
    * @return : true if ast contains any possible annotation, otherwise false
    */
   public static Boolean containsAnnotation(DetailAST ast, String fullAnnotation) {
      return getListOfSimpleAndFullName(fullAnnotation).stream()
                                                       .anyMatch(annotation -> AnnotationUtility.containsAnnotation(ast, annotation));
   }

   public static DetailAST getAnnotation(DetailAST ast, String fullAnnotation) {
      DetailAST simpleAnnotationAst = AnnotationUtility.getAnnotation(ast, getSimpleName(fullAnnotation));
      return nonNull(simpleAnnotationAst) ? simpleAnnotationAst : AnnotationUtility.getAnnotation(ast, fullAnnotation);
   }

   public static Boolean annotationContainsKey(DetailAST ast, String annotation, String key) {
      Boolean annotationContainsKey = false;
      DetailAST annotationAst = getAnnotation(ast, annotation);
      if (nonNull(annotationAst)) {
         List<String> annotationKeys = getAnnotationKeys(annotationAst);
         annotationContainsKey = annotationKeys.contains(key);
      }
      return annotationContainsKey;
   }

   public static List<String> getAnnotationKeys(DetailAST annotationAst) {
      List<DetailAST> keyValuePairAstList = findByType(annotationAst, TokenTypes.ANNOTATION_MEMBER_VALUE_PAIR);
      return keyValuePairAstList.stream()
                                .map(keyValuePairAst -> keyValuePairAst.getFirstChild().getText())
                                .collect(Collectors.toList());
   }

   public static Map<String, DetailAST> getAnnotationKeyPairAstMap(DetailAST annotationAst) {
      List<DetailAST> keyValuePairAstList = findByType(annotationAst, TokenTypes.ANNOTATION_MEMBER_VALUE_PAIR);
      return keyValuePairAstList.stream().collect(Collectors.toMap(keyValuePairAst -> keyValuePairAst.getFirstChild().getText(),
                                                                   Function.identity()));
   }

   public static Optional<String> getAnnotationValueAsString(DetailAST annotationKeyValueAst) {
      Optional<String> result = Optional.empty();
      DetailAST exprAst = annotationKeyValueAst.findFirstToken(TokenTypes.EXPR);
      if (nonNull(exprAst)) {
         DetailAST literalValueAst = exprAst.getFirstChild();
         if (nonNull(literalValueAst)) {
            result = Optional.of(literalValueAst.getText());
         }
      }
      return result;
   }

}
