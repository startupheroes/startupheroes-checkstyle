package startupheroes.checkstyle.checks.custom;

import com.google.common.base.Splitter;
import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import com.puppycrawl.tools.checkstyle.utils.AnnotationUtility;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static startupheroes.checkstyle.util.CommonUtil.findByType;

/**
 * @author ozlem.ulag
 */
public class BlockedAnnotationKeyCheck extends AbstractCheck {

   /**
    * A key is pointing to the warning message text in "messages.properties"
    * file.
    */
   static final String MSG_KEY = "blockedAnnotationKeyCheckMessage";

   private Map<String, String> annotationBlockedKeyMap = new HashMap<>();

   @Override
   public int[] getDefaultTokens() {
      return new int[]{TokenTypes.VARIABLE_DEF};
   }

   @Override
   public void visitToken(final DetailAST ast) {
      Set<String> annotations = annotationBlockedKeyMap.keySet();
      for (String annotation : annotations) {
         String blockedKey = annotationBlockedKeyMap.get(annotation);
         if (annotationContainsKey(ast, annotation, blockedKey)) {
            log(ast.getLineNo(), MSG_KEY, blockedKey, annotation);
         }
      }
   }

   private static Boolean annotationContainsKey(DetailAST ast, String annotation, String key) {
      Boolean annotationContainsKey = false;
      DetailAST annotationAst = AnnotationUtility.getAnnotation(ast, annotation);
      if (Objects.nonNull(annotationAst)) {
         List<DetailAST> keyValuePairAstList = findByType(annotationAst, TokenTypes.ANNOTATION_MEMBER_VALUE_PAIR);
         if (!keyValuePairAstList.isEmpty()) {
            List<String> annotationKeys = keyValuePairAstList.stream()
                                                             .map(keyValuePairAst -> keyValuePairAst.getFirstChild().getText())
                                                             .collect(Collectors.toList());
            annotationContainsKey = annotationKeys.contains(key);
         }
      }
      return annotationContainsKey;
   }

   public void setAnnotationBlockedKeyMap(String property) {
      this.annotationBlockedKeyMap = map(property, ",");
   }

   private static Map<String, String> map(String property, String splitter) {
      return Splitter.on(splitter).omitEmptyStrings().trimResults().withKeyValueSeparator(":").split(property);
   }

}
