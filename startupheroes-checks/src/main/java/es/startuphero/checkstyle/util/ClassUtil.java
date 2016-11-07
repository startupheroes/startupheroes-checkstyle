package es.startuphero.checkstyle.util;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import static java.util.Objects.nonNull;
import static es.startuphero.checkstyle.util.CommonUtil.getFullName;
import static es.startuphero.checkstyle.util.CommonUtil.getSimpleName;

/**
 * @author ozlem.ulag
 */
public final class ClassUtil {

   public static final String STRING_CLASS_NAME_BY_PACKAGE = "java.lang.String";

   public static final String OBJECT_CLASS_NAME_BY_PACKAGE = "java.lang.Object";

   public static final String ABSTRACT_CLASS_PREFIX = "Abstract";

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
    * @param rootAst root ast of branch
    * @return simple name -> full name of imports like Object -> java.lang.Object
    */
   public static Map<String, String> getImportSimpleFullNameMap(DetailAST rootAst) {
      Map<String, String> simpleFullNameMapOfImports = new LinkedHashMap<>();
      for (DetailAST sibling = rootAst; sibling != null; sibling = sibling.getNextSibling()) {
         if (sibling.getType() == TokenTypes.IMPORT) {
            simpleFullNameMapOfImports.put(CommonUtil.getSimpleName(sibling), CommonUtil.getFullName(sibling));
         }
      }
      return simpleFullNameMapOfImports;
   }

   public static Optional<String> getExtendedClassName(DetailAST classAst) {
      Optional<String> extendedClassName = Optional.empty();
      DetailAST extendClauseNode = classAst.findFirstToken(TokenTypes.EXTENDS_CLAUSE);
      if (nonNull(extendClauseNode)) {
         DetailAST extendedClassNameNode = extendClauseNode.getFirstChild();
         extendedClassName = Optional.of(extendedClassNameNode.getText());
      }
      return extendedClassName;
   }

   public static Boolean isExtendsAnotherClass(DetailAST classAst) {
      return nonNull(classAst.findFirstToken(TokenTypes.EXTENDS_CLAUSE));
   }

   /**
    * @param classAst class definition for check.
    * @return true if a given class declared as abstract.
    */
   public static Boolean isAbstract(DetailAST classAst) {
      return classAst.findFirstToken(TokenTypes.MODIFIERS)
                .branchContains(TokenTypes.ABSTRACT);
   }

}
