package es.startuphero.checkstyle.checks.annotation;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static es.startuphero.checkstyle.util.AnnotationUtils.getAnnotation;
import static es.startuphero.checkstyle.util.ClassUtils.isEntity;
import static es.startuphero.checkstyle.util.CommonUtils.getSimpleName;
import static es.startuphero.checkstyle.util.VariableUtils.getVariableNameAstMap;

/**
 * @author ozlem.ulag
 */
public class ColumnDefaultCheck extends AbstractCheck {

  /**
   * A key is pointing to the warning message text in "messages.properties" file.
   */
  private static final String NOT_MATCHING_MSG_KEY = "column.default.not.matching";

  private static final String ANNOTATION_DIRECT_EXPRESSION_MSG_KEY =
      "column.default.annotation.direct.expression";

  private static final String ANNOTATION_VALUE_REQUIRE_SINGLE_QUOTE_MSG_KEY =
      "column.default.annotation.value.require.single.quote";

  private static final String SINGLE_QUOTE = "'";

  private static final String DOUBLE_QUOTE = "\"";

  private static final String EMPTY = "";

  private static final List<Integer> TOKEN_TYPES_REQUIRE_SINGLE_QUOTE_ON_ANNOTATION_VALUE =
      List.of(TokenTypes.STRING_LITERAL,
              TokenTypes.CHAR_LITERAL,
              TokenTypes.LITERAL_CHAR,
              TokenTypes.IDENT); // case enum

  /**
   * set type annotation to understand that a class is an entity.
   */
  private String typeAnnotation;

  /**
   * set abstract type annotation to understand that a class is abstract.
   */
  private String abstractTypeAnnotation;

  private String columnDefaultAnnotation;

  private String excludedColumnDefaultAnnotationValueRegex;

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
    if (isEntity(ast, typeAnnotation) || isEntity(ast, abstractTypeAnnotation)) {
      Map<String, DetailAST> variableNameAstMap = getVariableNameAstMap(ast, false);
      for (String variable : variableNameAstMap.keySet()) {
        DetailAST variableAst = variableNameAstMap.get(variable);
        checkAssignValueAndAnnotationValueMatching(variable, variableAst, columnDefaultAnnotation);
      }
    }
  }

  private void checkAssignValueAndAnnotationValueMatching(String variable,
                                                          DetailAST variableAst,
                                                          String columnDefaultAnnotation) {
    DetailAST assignAst = variableAst.findFirstToken(TokenTypes.ASSIGN);
    DetailAST annotationAst = getAnnotation(variableAst, columnDefaultAnnotation);
    // assign value and annotation value not exists.
    if (assignAst == null && annotationAst == null) {
      return;
    }
    // assign value exists but annotation value not exists.
    if (annotationAst == null) {
      log(variableAst.getLineNo(), NOT_MATCHING_MSG_KEY, variable, getAssignValue(assignAst), null);
      return;
    }
    // annotation value param not allowed.
    DetailAST annotationExpressionAst = annotationAst.findFirstToken(TokenTypes.EXPR);
    if (annotationExpressionAst == null) {
      log(variableAst.getLineNo(), ANNOTATION_DIRECT_EXPRESSION_MSG_KEY, variable);
      return;
    }
    // skip assign value check if annotation value excluded.
    if (isExcludedVariable(getAnnotationValue(annotationAst, true))) {
      return;
    }
    // annotation value exists but assign value not exists.
    if (assignAst == null) {
      log(variableAst.getLineNo(), NOT_MATCHING_MSG_KEY, variable, null,
          getAnnotationValue(annotationAst, true));
      return;
    }
    // require single quote on string, char, enum variables
    if (requireSingleQuoteOnAnnotationValue(assignAst) &&
        !(getAnnotationValue(annotationAst, false).startsWith(SINGLE_QUOTE) &&
          getAnnotationValue(annotationAst, false).endsWith(SINGLE_QUOTE))) {
      log(variableAst.getLineNo(), ANNOTATION_VALUE_REQUIRE_SINGLE_QUOTE_MSG_KEY,
          getSimpleName(columnDefaultAnnotation),
          getAnnotationValue(annotationAst, true),
          variable);
      return;
    }
    // both assign value and annotation value should be same.
    String annotationValue = getAnnotationValue(annotationAst, true);
    String assignValue = getAssignValue(assignAst);
    if (!Objects.equals(annotationValue, assignValue)) {
      log(variableAst.getLineNo(), NOT_MATCHING_MSG_KEY, variable, assignValue, annotationValue);
    }
  }

  private Boolean isExcludedVariable(String annotationValue) {
    if (excludedColumnDefaultAnnotationValueRegex == null) {
      return false;
    }
    return annotationValue.matches(excludedColumnDefaultAnnotationValueRegex);
  }

  private Boolean requireSingleQuoteOnAnnotationValue(DetailAST assignAst) {
    DetailAST expressionAst = assignAst.findFirstToken(TokenTypes.EXPR);
    // case enum
    DetailAST dotDst = expressionAst.findFirstToken(TokenTypes.DOT);
    if (dotDst != null) {
      return true;
    } else {
      DetailAST valueAst = expressionAst.getFirstChild();
      return TOKEN_TYPES_REQUIRE_SINGLE_QUOTE_ON_ANNOTATION_VALUE.contains(valueAst.getType());
    }
  }

  private String getAssignValue(DetailAST assignAst) {
    DetailAST expressionAst = assignAst.findFirstToken(TokenTypes.EXPR);

    String assignValue;
    // case enum
    DetailAST dotDst = expressionAst.findFirstToken(TokenTypes.DOT);
    if (dotDst != null) {
      assignValue = dotDst.getLastChild().getText();
    } else {
      DetailAST valueAst = expressionAst.getFirstChild();
      assignValue = valueAst.getText();
      assignValue = trimAssignValue(assignValue, valueAst);
    }
    return assignValue;
  }

  private String trimAssignValue(String assignValue, DetailAST valueAst) {
    switch (valueAst.getType()) {
      case TokenTypes.STRING_LITERAL:
        assignValue = assignValue.replaceAll(DOUBLE_QUOTE, EMPTY);
        break;
      case TokenTypes.CHAR_LITERAL:
      case TokenTypes.LITERAL_CHAR:
        assignValue = assignValue.replaceAll(SINGLE_QUOTE, EMPTY);
        break;
      case TokenTypes.NUM_LONG:
        assignValue = assignValue.replace("l", EMPTY).replace("L", EMPTY);
        break;
      case TokenTypes.NUM_DOUBLE:
        assignValue = assignValue.replace("d", EMPTY).replace("D", EMPTY);
        break;
      case TokenTypes.NUM_FLOAT:
        assignValue = assignValue.replace("f", EMPTY).replace("F", EMPTY);
        break;
      default:
        // integer, boolean, enum..
        break;
    }
    return assignValue;
  }

  private String getAnnotationValue(DetailAST annotationAst, Boolean skipSingleQuotes) {
    DetailAST expressionAst = annotationAst.findFirstToken(TokenTypes.EXPR);
    String annotationValue = expressionAst.getFirstChild().getText();
    annotationValue = annotationValue.replaceAll(DOUBLE_QUOTE, EMPTY);
    if (skipSingleQuotes) {
      annotationValue = annotationValue.replaceAll(SINGLE_QUOTE, EMPTY);
    }
    return annotationValue;
  }

  public void setTypeAnnotation(String typeAnnotation) {
    this.typeAnnotation = typeAnnotation;
  }

  public void setAbstractTypeAnnotation(String abstractTypeAnnotation) {
    this.abstractTypeAnnotation = abstractTypeAnnotation;
  }

  public void setColumnDefaultAnnotation(String columnDefaultAnnotation) {
    this.columnDefaultAnnotation = columnDefaultAnnotation;
  }

  public void setExcludedColumnDefaultAnnotationValueRegex(String excludedColumnDefaultAnnotationValueRegex) {
    this.excludedColumnDefaultAnnotationValueRegex = excludedColumnDefaultAnnotationValueRegex;
  }
}
