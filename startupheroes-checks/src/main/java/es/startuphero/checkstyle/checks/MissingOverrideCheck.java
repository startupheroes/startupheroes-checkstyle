package es.startuphero.checkstyle.checks;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;

import static es.startuphero.checkstyle.util.AnnotationUtil.OVERRIDE_ANNOTATION_NAME_BY_PACKAGE;
import static es.startuphero.checkstyle.util.AnnotationUtil.hasAnnotation;
import static es.startuphero.checkstyle.util.ClassUtil.getClassName;
import static es.startuphero.checkstyle.util.ClassUtil.getClassOf;
import static es.startuphero.checkstyle.util.ClassUtil.getImportSimpleFullNameMap;
import static es.startuphero.checkstyle.util.CommonUtil.getPackageName;
import static es.startuphero.checkstyle.util.MethodUtil.getMethodName;
import static es.startuphero.checkstyle.util.MethodUtil.getParameterTypes;
import static es.startuphero.checkstyle.util.MethodUtil.isMethodOverriden;

/**
 * @author ozlem.ulag
 */
public class MissingOverrideCheck extends AbstractCheck {

  /**
   * A key is pointing to the warning message text in "messages.properties" file.
   */
  private static final String MSG_KEY = "missing.override";

  private String packageName;

  private Map<String, String> importSimpleFullNameMap;

  @Override
  public int[] getDefaultTokens() {
    return getAcceptableTokens();
  }

  @Override
  public int[] getAcceptableTokens() {
    return new int[] {TokenTypes.METHOD_DEF};
  }

  @Override
  public int[] getRequiredTokens() {
    return getAcceptableTokens();
  }

  @Override
  public void beginTree(DetailAST rootAST) {
    packageName = getPackageName(rootAST);
    importSimpleFullNameMap = getImportSimpleFullNameMap(rootAST);
  }

  @Override
  public void visitToken(DetailAST ast) {
    try {
      Optional<DetailAST> classNode = getClassOf(ast);
      if (classNode.isPresent()) {
        String className = getClassName(classNode.get());
        String methodName = getMethodName(ast);
        Class<?> classOfMethod = Class.forName(packageName + "." + className);
        Method method = classOfMethod.getDeclaredMethod(methodName,
            getParameterTypes(ast, importSimpleFullNameMap));
        if (isMethodOverriden(method) && !hasAnnotation(ast, OVERRIDE_ANNOTATION_NAME_BY_PACKAGE)) {
          log(ast.getLineNo(), MSG_KEY);
        }
      }
    } catch (Exception ignored) {
    }
  }
}
