package startupheroes.checkstyle.checks.custom;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static startupheroes.checkstyle.util.AnnotationUtil.annotationContainsKey;
import static startupheroes.checkstyle.util.CommonUtil.getKeyValueMap;
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
      return new int[]{TokenTypes.VARIABLE_DEF};
   }

   @Override
   public void visitToken(final DetailAST ast) {
      Set<String> annotations = annotationBlockedKeyMap.keySet();
      for (String annotation : annotations) {
         String blockedKey = annotationBlockedKeyMap.get(annotation);
         if (annotationContainsKey(ast, annotation, blockedKey)) {
            log(ast.getLineNo(), MSG_KEY, blockedKey, getSimpleName(annotation));
         }
      }
   }

   public void setAnnotationBlockedKeyMap(String property) {
      this.annotationBlockedKeyMap = getKeyValueMap(property);
   }

}
