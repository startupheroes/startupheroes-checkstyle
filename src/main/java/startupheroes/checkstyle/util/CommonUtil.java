package startupheroes.checkstyle.util;

import com.google.common.base.Splitter;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author ozlem.ulag
 */
public final class CommonUtil {

   private CommonUtil() {
   }

   /**
    * finds matching AST's that has type given type from given ast.
    */
   public static List<DetailAST> findByType(DetailAST ast, int type) {
      List<DetailAST> astListByType = new ArrayList<>();
      for (DetailAST child = ast.getFirstChild(); child != null; child = child.getNextSibling()) {
         if (child.getType() == type) {
            astListByType.add(child);
         }
      }
      return astListByType;
   }

   public static Map<String, String> getKeyValueMap(String property) {
      return Splitter.on(",").omitEmptyStrings().trimResults().withKeyValueSeparator(":").split(property);
   }

   /**
    * @param fullName Full package qualifier of any type of object like annotation, class, interface like java.lang.Object
    * @return list of simple name and full qualifier {Object, java.lang.Object}
    */
   public static List<String> getListOfSimpleAndFullName(String fullName) {
      String[] packageNames = fullName.split("\\.");
      String annotationName = packageNames[packageNames.length - 1];
      return Arrays.asList(annotationName, fullName);
   }

   /**
    * @param fullName Full qualifier name like java.lang.Object
    * @return simple name like {Object}
    */
   public static String getSimpleName(String fullName) {
      String[] packageNames = fullName.split("\\.");
      return packageNames[packageNames.length - 1];
   }

}
