package startupheroes.checkstyle.checks.custom;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import com.puppycrawl.tools.checkstyle.utils.AnnotationUtility;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * @author ozlem.ulag
 */
public class RedundantMultipleAnnotationCheck extends AbstractCheck {

   /**
    * A key is pointing to the warning message text in "messages.properties"
    * file.
    */
   static final String MSG_KEY = "redundantMultipleAnnotationCheckMessage";

   private Set<String> annotationSet1 = new HashSet<>();

   private Set<String> annotationSet2 = new HashSet<>();

   @Override
   public int[] getDefaultTokens() {
      return new int[]{
          TokenTypes.CLASS_DEF,
          TokenTypes.INTERFACE_DEF,
          TokenTypes.ENUM_DEF,
          TokenTypes.METHOD_DEF,
          TokenTypes.CTOR_DEF,
          TokenTypes.VARIABLE_DEF,
          };
   }

   @Override
   public void visitToken(final DetailAST ast) {
      Optional<String> annotation1 = findAnnotation(ast, annotationSet1);
      Optional<String> annotation2 = findAnnotation(ast, annotationSet2);
      if (annotation1.isPresent() && annotation2.isPresent()) {
         log(ast.getLineNo(), MSG_KEY, annotation1.get(), annotation2.get());
      }
   }

   private static Optional<String> findAnnotation(DetailAST ast, Set<String> annotationSet) {
      return annotationSet.stream().filter(annotation -> AnnotationUtility.containsAnnotation(ast, annotation)).findFirst();
   }

   public void setAnnotationSet1(String... annotationSet1) {
      Collections.addAll(this.annotationSet1, annotationSet1);
   }

   public void setAnnotationSet2(String... annotationSet2) {
      Collections.addAll(this.annotationSet2, annotationSet2);
   }

}
