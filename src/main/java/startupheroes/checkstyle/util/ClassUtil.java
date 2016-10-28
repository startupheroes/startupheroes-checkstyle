package startupheroes.checkstyle.util;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import static startupheroes.checkstyle.util.CommonUtil.getFullName;
import static startupheroes.checkstyle.util.CommonUtil.getSimpleName;

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
      return AnnotationUtil.hasAnnotation(classAst, fullEntityAnnotation);
   }

   /**
    * @param classAst : required to be CLASS_DEF type
    * @return name of the class
    */
   public static String getClassName(DetailAST classAst) {
      DetailAST type = classAst.findFirstToken(TokenTypes.LITERAL_CLASS);
      return type.getNextSibling().getText();
   }

   /**
    * @param anyAstBelowClass : any type of ast
    * @return class Ast of given ast if exists
    */
   public static Optional<DetailAST> getClassOf(DetailAST anyAstBelowClass) {
      Optional<DetailAST> classAst = Optional.empty();
      for (DetailAST parent = anyAstBelowClass; parent != null; parent = parent.getParent()) {
         if (parent.getType() == TokenTypes.CLASS_DEF) {
            classAst = Optional.of(parent);
            break;
         }
      }
      return classAst;
   }

   /**
    * @param rootAst Require to be CLASS_DEF type
    * @return simple name -> full name of imports like Object -> java.lang.Object
    */
   public static Map<String, String> getImportSimpleFullNameMap(DetailAST rootAst) {
      Map<String, String> simpleFullNameMapOfImports = new LinkedHashMap<>();
      for (DetailAST sibling = rootAst; sibling != null; sibling = sibling.getNextSibling()) {
         if (sibling.getType() == TokenTypes.IMPORT) {
            simpleFullNameMapOfImports.put(getSimpleName(sibling), getFullName(sibling));
         }
      }
      return simpleFullNameMapOfImports;
   }

}
