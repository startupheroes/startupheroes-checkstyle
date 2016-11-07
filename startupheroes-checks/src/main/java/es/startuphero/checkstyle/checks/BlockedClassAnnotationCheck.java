package es.startuphero.checkstyle.checks;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import es.startuphero.checkstyle.util.AnnotationUtil;
import es.startuphero.checkstyle.util.CommonUtil;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static es.startuphero.checkstyle.util.CommonUtil.getSimpleName;

/**
 * @author ozlem.ulag
 */
public class BlockedClassAnnotationCheck extends AbstractCheck {

   /**
    * A key is pointing to the warning message text in "messages.properties" file.
    */
   private static final String MSG_KEY = "blockedClassAnnotationCheckMessage";

   private Set<String> blockedAnnotations = new HashSet<>();

   @Override
   public int[] getDefaultTokens() {
      return getAcceptableTokens();
   }

   @Override
   public int[] getAcceptableTokens() {
      return new int[]{TokenTypes.CLASS_DEF, TokenTypes.INTERFACE_DEF};
   }

   @Override
   public int[] getRequiredTokens() {
      return getAcceptableTokens();
   }

   @Override
   public void visitToken(DetailAST ast) {
      blockedAnnotations.stream()
                        .filter(blockedAnnotation -> AnnotationUtil.hasAnnotation(ast, blockedAnnotation))
                        .forEach(blockedAnnotation -> log(AnnotationUtil.getAnnotation(ast, blockedAnnotation).getLineNo(),
                                                          MSG_KEY,
                                                          CommonUtil.getSimpleName(blockedAnnotation)));
   }

   public void setBlockedAnnotations(String... blockedAnnotations) {
      Collections.addAll(this.blockedAnnotations, blockedAnnotations);
   }

}
