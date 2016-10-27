package startupheroes.checkstyle.util;

import com.puppycrawl.tools.checkstyle.api.DetailAST;

/**
 * @author ozlem.ulag
 */
public final class ClassUtil {

   private ClassUtil() {
   }

   /**
    * @param classAst required to be CLASS_DEF type
    * @param fullEntityAnnotation : full entity annotation like javax.persistence.Entity
    * @return true if class is an entity, otherwise false
    */
   public static Boolean isEntity(DetailAST classAst, String fullEntityAnnotation) {
      return AnnotationUtil.containsAnnotation(classAst, fullEntityAnnotation);
   }

}
