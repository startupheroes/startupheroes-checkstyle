package startupheroes.checkstyle.util;

import antlr.collections.AST;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.FullIdent;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import com.puppycrawl.tools.checkstyle.utils.CheckUtils;
import java.util.List;
import java.util.stream.Collectors;

import static startupheroes.checkstyle.util.CommonUtil.getChildsByType;
import static startupheroes.checkstyle.util.CommonUtil.getSimpleAndFullNames;

/**
 * @author ozlem.ulag
 */
public final class MethodUtil {

   private static final String STRING_FULL_NAME = "java.lang.String";

   private static final String TO_STRING_METHOD_NAME = "toString";

   private static final String HASH_CODE_METHOD_NAME = "hashCode";

   private static final String OBJECT_FULL_NAME = "java.lang.Object";

   private MethodUtil() {
   }

   /**
    * @param classAst : required to be CLASS_DEF type
    * @return method asts
    */
   public static List<DetailAST> getMethods(DetailAST classAst) {
      DetailAST objBlock = classAst.findFirstToken(TokenTypes.OBJBLOCK);
      return getChildsByType(objBlock, TokenTypes.METHOD_DEF);
   }

   /**
    * @param methodAst : required to be METHOD_DEF type
    * @return name of the method
    */
   public static String getMethodName(DetailAST methodAst) {
      DetailAST type = methodAst.findFirstToken(TokenTypes.TYPE);
      return type.getNextSibling().getText();
   }

   public static List<DetailAST> getGetters(DetailAST classAst) {
      return getMethods(classAst).stream().filter(CheckUtils::isGetterMethod).collect(Collectors.toList());
   }

   public static List<DetailAST> getSetters(DetailAST classAst) {
      return getMethods(classAst).stream().filter(CheckUtils::isSetterMethod).collect(Collectors.toList());
   }

   /**
    * Determines if an AST is a valid Equals method implementation.
    *
    * @param ast the AST to check
    * @return true if the {code ast} is a Equals method.
    */
   public static Boolean isEqualsMethod(DetailAST ast) {
      final DetailAST modifiers = ast.getFirstChild();
      final DetailAST parameters = ast.findFirstToken(TokenTypes.PARAMETERS);

      return CheckUtils.isEqualsMethod(ast)
             && modifiers.branchContains(TokenTypes.LITERAL_PUBLIC)
             && isObjectParam(parameters.getFirstChild())
             && (ast.branchContains(TokenTypes.SLIST)
                 || modifiers.branchContains(TokenTypes.LITERAL_NATIVE));
   }

   /**
    * Determines if an AST is a valid HashCode method implementation.
    *
    * @param ast the AST to check
    * @return true if the {code ast} is a HashCode method.
    */
   public static Boolean isHashCodeMethod(DetailAST ast) {
      final DetailAST modifiers = ast.getFirstChild();
      final AST type = ast.findFirstToken(TokenTypes.TYPE);
      final AST methodName = ast.findFirstToken(TokenTypes.IDENT);
      final DetailAST parameters = ast.findFirstToken(TokenTypes.PARAMETERS);

      return type.getFirstChild().getType() == TokenTypes.LITERAL_INT
             && HASH_CODE_METHOD_NAME.equals(methodName.getText())
             && modifiers.branchContains(TokenTypes.LITERAL_PUBLIC)
             && !modifiers.branchContains(TokenTypes.LITERAL_STATIC)
             && parameters.getFirstChild() == null
             && (ast.branchContains(TokenTypes.SLIST)
                 || modifiers.branchContains(TokenTypes.LITERAL_NATIVE));
   }

   /**
    * Determines if an AST is a valid toString method implementation.
    *
    * @param ast the AST to check
    * @return true if the {code ast} is a toString method.
    */
   public static Boolean isToStringMethod(DetailAST ast) {
      final DetailAST modifiers = ast.getFirstChild();
      final AST type = ast.findFirstToken(TokenTypes.TYPE);
      final AST methodName = ast.findFirstToken(TokenTypes.IDENT);
      final DetailAST parameters = ast.findFirstToken(TokenTypes.PARAMETERS);

      return getSimpleAndFullNames(STRING_FULL_NAME).contains(type.getFirstChild().getText())
             && TO_STRING_METHOD_NAME.equals(methodName.getText())
             && modifiers.branchContains(TokenTypes.LITERAL_PUBLIC)
             && !modifiers.branchContains(TokenTypes.LITERAL_STATIC)
             && parameters.getFirstChild() == null
             && (ast.branchContains(TokenTypes.SLIST)
                 || modifiers.branchContains(TokenTypes.LITERAL_NATIVE));
   }

   /**
    * Determines if an AST is a formal param of type Object.
    *
    * @param paramNode the AST to check
    * @return true if firstChild is a parameter of an Object type.
    */
   private static Boolean isObjectParam(DetailAST paramNode) {
      final DetailAST typeNode = paramNode.findFirstToken(TokenTypes.TYPE);
      final FullIdent fullIdent = FullIdent.createFullIdentBelow(typeNode);
      final String name = fullIdent.getText();
      return getSimpleAndFullNames(OBJECT_FULL_NAME).contains(name);
   }

}
