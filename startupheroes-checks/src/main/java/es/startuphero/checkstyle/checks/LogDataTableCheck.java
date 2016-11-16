package es.startuphero.checkstyle.checks;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import com.puppycrawl.tools.checkstyle.utils.CommonUtils;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import static es.startuphero.checkstyle.util.AnnotationUtil.getAnnotation;
import static es.startuphero.checkstyle.util.AnnotationUtil.getKeyValueAstMap;
import static es.startuphero.checkstyle.util.AnnotationUtil.getValueAsString;
import static es.startuphero.checkstyle.util.ClassUtil.getClassName;
import static es.startuphero.checkstyle.util.ClassUtil.isEntity;
import static es.startuphero.checkstyle.util.VariableUtil.getNonStaticVariables;
import static es.startuphero.checkstyle.util.VariableUtil.getVariableName;
import static java.util.Objects.nonNull;

/**
 * @author ozlem.ulag
 */
public class LogDataTableCheck extends AbstractCheck {

  /**
   * A key is pointing to the warning message text in "messages.properties" file.
   */
  private static final String MSG_KEY = "keep.log.data.table";

  /**
   * set type annotation to understand that a class is an entity.
   */
  private String typeAnnotation;

  private String abstractTypeAnnotation;

  private String columnAnnotation;

  private Integer limitLength;

  /** The format string of the regexp. */
  private String classNameFormat = "Log$";

  /** The regexp to match against. */
  private Pattern regexp = Pattern.compile(classNameFormat);

  public void setTypeAnnotation(String typeAnnotation) {
    this.typeAnnotation = typeAnnotation;
  }

  public void setAbstractTypeAnnotation(String abstractTypeAnnotation) {
    this.abstractTypeAnnotation = abstractTypeAnnotation;
  }

  public void setColumnAnnotation(String columnAnnotation) {
    this.columnAnnotation = columnAnnotation;
  }

  public void setLimitLength(Integer limitLength) {
    this.limitLength = limitLength;
  }

  /**
   * Set the format to the specified regular expression.
   *
   * @param format a {@code String} value
   */
  public void setClassNameFormat(String format) {
    this.classNameFormat = format;
    regexp = CommonUtils.createPattern(format);
  }

  @Override
  public int[] getDefaultTokens() {
    return getAcceptableTokens();
  }

  @Override
  public int[] getAcceptableTokens() {
    return new int[] {TokenTypes.CLASS_DEF};
  }

  @Override
  public int[] getRequiredTokens() {
    return getAcceptableTokens();
  }

  @Override
  public void visitToken(DetailAST ast) {
    assertions();
    if (isEntity(ast, typeAnnotation) || isEntity(ast, abstractTypeAnnotation)) {
      String className = getClassName(ast);
      if (isMatchingClassName(className)) {
        List<DetailAST> variableNodes = getNonStaticVariables(ast);
        variableNodes.forEach(this::checkVariableLength);
      }
    }
  }

  private void assertions() {
    Assert.isTrue(!StringUtils.isEmpty(typeAnnotation));
    Assert.isTrue(!StringUtils.isEmpty(abstractTypeAnnotation));
    Assert.isTrue(!StringUtils.isEmpty(columnAnnotation));
    Assert.isTrue(!StringUtils.isEmpty(limitLength));
  }

  /**
   * @param className class name for check.
   * @return true if class name matches format of any class names.
   */
  private boolean isMatchingClassName(String className) {
    return regexp.matcher(className).find();
  }

  private void checkVariableLength(DetailAST variableNode) {
    DetailAST columnAnnotationNode = getAnnotation(variableNode, columnAnnotation);
    if (nonNull(columnAnnotationNode)) {
      Map<String, DetailAST> keyValueAstMap = getKeyValueAstMap(columnAnnotationNode);
      DetailAST lengthKeyValueNode = keyValueAstMap.get("length");
      if (nonNull(lengthKeyValueNode)) {
        Optional<String> valueAsString = getValueAsString(lengthKeyValueNode);
        if (valueAsString.isPresent()) {
          Integer variableLength = Integer.valueOf(valueAsString.get());
          if (variableLength > limitLength) {
            log(variableNode.getLineNo(), MSG_KEY, getVariableName(variableNode), limitLength);
          }
        }
      }
    }
  }
}
