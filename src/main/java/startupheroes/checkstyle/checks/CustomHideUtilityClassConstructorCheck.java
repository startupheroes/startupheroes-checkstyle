package startupheroes.checkstyle.checks;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.checks.design.HideUtilityClassConstructorCheck;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import startupheroes.checkstyle.util.AnnotationUtil;

/**
 * @author ozlem.ulag
 */
public class CustomHideUtilityClassConstructorCheck extends HideUtilityClassConstructorCheck {

   private Set<String> exceptionalAnnotations = new HashSet<>();

   @Override
   public void visitToken(DetailAST ast) {
      Boolean hasExceptionalAnnotation = exceptionalAnnotations.stream()
                                                               .anyMatch(exceptionalAnnotation ->
                                                                             AnnotationUtil.hasAnnotation(ast, exceptionalAnnotation));
      if (!hasExceptionalAnnotation) {
         super.visitToken(ast);
      }
   }

   public void setExceptionalAnnotations(String... exceptionalAnnotations) {
      Collections.addAll(this.exceptionalAnnotations, exceptionalAnnotations);
   }

}
