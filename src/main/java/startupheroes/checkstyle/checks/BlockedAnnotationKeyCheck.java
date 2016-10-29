package startupheroes.checkstyle.checks;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static startupheroes.checkstyle.util.AnnotationUtil.getKeys;
import static startupheroes.checkstyle.util.CommonUtil.splitProperty;
import static startupheroes.checkstyle.util.CommonUtil.getSimpleName;

/**
 * @author ozlem.ulag
 */
public class BlockedAnnotationKeyCheck extends AbstractCheck {

   /**
    * A key is pointing to the warning message text in "messages.properties" file.
    */
   private static final String MSG_KEY = "blockedAnnotationKeyCheckMessage";

   private Map<String, String> annotationBlockedKeyMap = new HashMap<>();

   @Override
   public int[] getDefaultTokens() {
      return getAcceptableTokens();
   }

   @Override
   public int[] getAcceptableTokens() {
      return new int[]{TokenTypes.ANNOTATION};
   }

   @Override
   public int[] getRequiredTokens() {
      return getAcceptableTokens();
   }

   @Override
   public void visitToken(DetailAST ast) {
      Set<String> checkedAnnotations = annotationBlockedKeyMap.keySet();
      String annotationSimpleName = getSimpleName(ast);
      checkedAnnotations.stream()
                        .filter(checkedAnnotation -> getSimpleName(checkedAnnotation).equals(annotationSimpleName))
                        .forEach(checkedAnnotation -> {
                           String blockedKey = annotationBlockedKeyMap.get(checkedAnnotation);
                           if (getKeys(ast).contains(blockedKey)) {
                              log(ast.getLineNo(), MSG_KEY, blockedKey, annotationSimpleName);
                           }
                        });
   }

   public void setAnnotationBlockedKeyMap(String property) {
      this.annotationBlockedKeyMap = splitProperty(property);
   }

}
