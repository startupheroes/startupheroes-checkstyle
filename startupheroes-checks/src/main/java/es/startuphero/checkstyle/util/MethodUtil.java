package es.startuphero.checkstyle.util;

import antlr.collections.AST;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.FullIdent;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import com.puppycrawl.tools.checkstyle.utils.CheckUtils;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static es.startuphero.checkstyle.util.CommonUtil.getFullName;

/**
 * @author ozlem.ulag
 */
public final class MethodUtil {

   private static final String TO_STRING_METHOD_NAME = "toString";

   private static final String HASH_CODE_METHOD_NAME = "hashCode";

   private static final String GET = "get";

   /** Pattern matching names of getter methods. */
   public static final String GETTER_PREFIX_REGEX = "^" + GET;

   private static final String SET = "set";

   /** Pattern matching names of setter methods. */
   public static final String SETTER_PREFIX_REGEX = "^" + SET;

   private MethodUtil() {
   }

   /**
    * @param classAst : required to be CLASS_DEF type
    * @return method asts
    */
   public static List<DetailAST> getMethods(DetailAST classAst) {
      DetailAST objBlock = classAst.findFirstToken(TokenTypes.OBJBLOCK);
      return CommonUtil.getChildsByType(objBlock, TokenTypes.METHOD_DEF);
   }

   /**
    * @param methodAst : required to be METHOD_DEF type
    * @return name of the method
    */
   public static String getMethodName(DetailAST methodAst) {
      DetailAST type = methodAst.findFirstToken(TokenTypes.TYPE);
      return type.getNextSibling().getText();
   }

   /**
    * @param methodAst : required to be METHOD_DEF type
    * @return list of class types of each parameters of method.
    */
   public static Class<?>[] getParameterTypes(DetailAST methodAst, Map<String, String> importSimpleFullNameMap) {
      DetailAST parameters = methodAst.findFirstToken(TokenTypes.PARAMETERS);
      List<DetailAST> parameterNodes = CommonUtil.getChildsByType(parameters, TokenTypes.PARAMETER_DEF);
      List<Class<?>> parameterTypes = new ArrayList<>();
      for (DetailAST parameterNode : parameterNodes) {
         String paramTypeSimpleName = getParamTypeSimpleName(parameterNode);
         String paramTypeFullName = CommonUtil.getFullName(methodAst, importSimpleFullNameMap, paramTypeSimpleName);
         try {
            Class<?> parameterClass = Class.forName(paramTypeFullName);
            parameterTypes.add(parameterClass);
         } catch (ClassNotFoundException ignored) {
         }
      }
      return parameterTypes.stream().toArray(Class<?>[]::new);
   }

   private static String getParamTypeSimpleName(DetailAST paramNode) {
      DetailAST typeNode = paramNode.findFirstToken(TokenTypes.TYPE);
      return FullIdent.createFullIdentBelow(typeNode).getText();
   }

   public static Boolean isMethodOverriden(Method method) {
      Class<?> declaringClass = method.getDeclaringClass();
      if (declaringClass.equals(Object.class)) {
         return false;
      }
      try {
         declaringClass.getSuperclass().getDeclaredMethod(method.getName(), method.getParameterTypes());
         return true;
      } catch (NoSuchMethodException ex) {
         for (Class<?> implementedInterface : declaringClass.getInterfaces()) {
            try {
               implementedInterface.getDeclaredMethod(method.getName(), method.getParameterTypes());
               return true;
            } catch (NoSuchMethodException ignored) {
            }
         }
         return false;
      }
   }

   public static List<DetailAST> getGetters(DetailAST classAst) {
      return getMethods(classAst).stream()
                                 .filter(getter -> CheckUtils.isGetterMethod(getter) && getMethodName(getter).startsWith(GET))
                                 .collect(Collectors.toList());
   }

   public static List<DetailAST> getSetters(DetailAST classAst) {
      return getMethods(classAst).stream()
                                 .filter(setter -> CheckUtils.isSetterMethod(setter) && getMethodName(setter).startsWith(SET))
                                 .collect(Collectors.toList());
   }

   /**
    * Determines if an AST is a valid Equals method implementation.
    *
    * @param ast the AST to check
    * @return true if the {code ast} is a Equals method.
    */
   public static Boolean isEqualsMethod(DetailAST ast) {
      DetailAST modifiers = ast.getFirstChild();
      DetailAST parameters = ast.findFirstToken(TokenTypes.PARAMETERS);

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
      DetailAST modifiers = ast.getFirstChild();
      AST type = ast.findFirstToken(TokenTypes.TYPE);
      AST methodName = ast.findFirstToken(TokenTypes.IDENT);
      DetailAST parameters = ast.findFirstToken(TokenTypes.PARAMETERS);

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
      DetailAST modifiers = ast.getFirstChild();
      AST type = ast.findFirstToken(TokenTypes.TYPE);
      AST methodName = ast.findFirstToken(TokenTypes.IDENT);
      DetailAST parameters = ast.findFirstToken(TokenTypes.PARAMETERS);

      return CommonUtil.getSimpleAndFullNames(ClassUtil.STRING_CLASS_NAME_BY_PACKAGE).contains(type.getFirstChild().getText())
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
      return CommonUtil.getSimpleAndFullNames(ClassUtil.OBJECT_CLASS_NAME_BY_PACKAGE).contains(getParamTypeSimpleName(paramNode));
   }

}
