package startupheroes.checkstyle.util;

import com.google.common.base.Splitter;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.FullIdent;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.util.Objects.isNull;

/**
 * @author ozlem.ulag
 */
public final class CommonUtil {

   private CommonUtil() {
   }

   /**
    * finds matching AST's that has type given type from given ast.
    */
   public static List<DetailAST> getChildsByType(DetailAST ast, int type) {
      List<DetailAST> astListByType = new ArrayList<>();
      for (DetailAST child = ast.getFirstChild(); child != null; child = child.getNextSibling()) {
         if (child.getType() == type) {
            astListByType.add(child);
         }
      }
      return astListByType;
   }

   public static Map<String, String> splitProperty(String property) {
      return Splitter.on(",").omitEmptyStrings().trimResults().withKeyValueSeparator(":").split(property);
   }

   /**
    * @param fullName Full qualifier name like java.lang.Object
    * @return simple name like {Object}
    */
   public static String getSimpleName(String fullName) {
      String[] packageNames = fullName.split("\\.");
      return packageNames[packageNames.length - 1];
   }

   /**
    * @return simple name(last name after last DOT if exists)
    */
   public static String getSimpleName(DetailAST ast) {
      DetailAST identifier = ast.findFirstToken(TokenTypes.IDENT);
      if (isNull(identifier) && ast.branchContains(TokenTypes.DOT)) {
         DetailAST dotAst = ast.findFirstToken(TokenTypes.DOT);
         identifier = dotAst.findFirstToken(TokenTypes.IDENT);
      }
      return identifier.getText();
   }

   public static String getFullName(DetailAST ast) {
      return FullIdent.createFullIdent(ast.findFirstToken(TokenTypes.DOT)).getText();
   }

   /**
    * @param fullName Full package qualifier of any type of object like annotation, class, interface like java.lang.Object
    * @return list of simple name and full qualifier {Object, java.lang.Object}
    */
   public static List<String> getSimpleAndFullNames(String fullName) {
      return Arrays.asList(getSimpleName(fullName), fullName);
   }

}
