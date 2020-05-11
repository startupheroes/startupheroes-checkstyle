package es.startuphero.checkstyle.checks.naming;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import es.startuphero.checkstyle.util.AnnotationUtils;
import es.startuphero.checkstyle.util.CommonUtils;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

import static es.startuphero.checkstyle.util.AnnotationUtils.getKeyValueAstMap;
import static es.startuphero.checkstyle.util.AnnotationUtils.getValueAsAnnotations;
import static es.startuphero.checkstyle.util.AnnotationUtils.getValueAsString;
import static es.startuphero.checkstyle.util.AnnotationUtils.hasAnnotation;
import static es.startuphero.checkstyle.util.ClassUtils.getClassName;
import static es.startuphero.checkstyle.util.CommonUtils.getSplitterOnComma;
import static java.util.Objects.nonNull;
import static java.util.Optional.empty;

/**
 * @author ozlem.ulag
 */
public class TableIdentifierNameCheck extends AbstractCheck {

  /**
   * A key is pointing to the warning message text in "messages.properties" file.
   */
  private static final String MSG_KEY = "illegal.table.identifier.name";

  private static final String MSG_IDENTIFIER_NAME_TOO_LONG = "table.identifier.name.too.long";

  private static final String TOO_LONG_IDENTIFIER_SUGGESTED_PREFIX = "your_table_name_your_column_names_";

  /**
   * set table annotation to check identifiers inside table.
   */
  private String tableAnnotation;

  private String identifierAnnotation;

  private String key;

  private String keyName;

  private String keyColumns;

  /**
   * set convenient suffix to add to the end of the index name.
   */
  private String suggestedSuffix;

  /**
   * set max length of table identifier name.
   */
  private Integer maxLength;

  /**
   * set regex for identifier name.
   */
  private String regex;

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
    if (hasAnnotation(ast, tableAnnotation)) {
      Map<String, DetailAST> tableKeyValueAstMap =
          getKeyValueAstMap(AnnotationUtils.getAnnotation(ast, tableAnnotation));
      if (tableKeyValueAstMap.containsKey(key)) {
        DetailAST keyValuePairNode = tableKeyValueAstMap.get(key);
        List<DetailAST> identifierAnnotationNodes = getValueAsAnnotations(keyValuePairNode);
        String className = getClassName(ast);
        identifierAnnotationNodes.forEach(
            identifierAnnotationNode -> checkIdentifierName(className, identifierAnnotationNode));
      }
    }
  }

  private void checkIdentifierName(String className, DetailAST identifierAnnotationNode) {
    Map<String, DetailAST> identifierKeyValueAstMap = getKeyValueAstMap(identifierAnnotationNode);
    DetailAST nameKeyValueAst = identifierKeyValueAstMap.get(keyName);
    Optional<String> identifierNameOptional = nonNull(nameKeyValueAst) ? getValueAsString(nameKeyValueAst) : empty();
    String identifierName = identifierNameOptional.orElse("");
    checkBySuggestedName(className, identifierAnnotationNode, identifierKeyValueAstMap, identifierName);
  }

  private void checkBySuggestedName(String className, DetailAST identifierAnnotationNode,
                                    Map<String, DetailAST> identifierKeyValueAstMap, String identifierName) {
    DetailAST columnsKeyValueAst = identifierKeyValueAstMap.get(keyColumns);
    if (nonNull(columnsKeyValueAst)) {
      List<String> columnNames = AnnotationUtils.getValueAsStringList(columnsKeyValueAst);
      if (columnNames.isEmpty()) {
        return;
      }
      if (identifierName.length() > maxLength) {
        log(identifierAnnotationNode.getLineNo(), MSG_IDENTIFIER_NAME_TOO_LONG, identifierName, maxLength);
        return;
      }
      String suggestedIdentifierName = getSuggestedIdentifierName(className, columnNames);
      if (suggestedIdentifierName.length() <= maxLength && !suggestedIdentifierName.equals(identifierName)) {
        log(identifierAnnotationNode.getLineNo(), MSG_KEY, suggestedIdentifierName);
        return;
      }
      if (!acceptableIdentifierName(identifierName)) {
        String suggestedIdentifierNameForLongNames = TOO_LONG_IDENTIFIER_SUGGESTED_PREFIX.concat(suggestedSuffix);
        log(identifierAnnotationNode.getLineNo(), MSG_KEY, suggestedIdentifierNameForLongNames);
      }
    }
  }

  private String getSuggestedIdentifierName(String className, List<String> columnNames) {
    return CommonUtils.convertToDatabaseForm(className, suggestedSuffix,
                                             columnNames.size() == 1 ? getSplitterOnComma().split(columnNames.get(0))
                                                 : columnNames);
  }

  private Boolean acceptableIdentifierName(String identifierName) {
    return Pattern.compile(regex).matcher(identifierName).matches() &&
           identifierName.endsWith(CommonUtils.getDatabaseIdentifierName("_" + suggestedSuffix));
  }

  public void setTableAnnotation(String tableAnnotation) {
    this.tableAnnotation = tableAnnotation;
  }

  public void setIdentifierAnnotation(String identifierAnnotation) {
    this.identifierAnnotation = identifierAnnotation;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public void setKeyName(String keyName) {
    this.keyName = keyName;
  }

  public void setKeyColumns(String keyColumns) {
    this.keyColumns = keyColumns;
  }

  public void setSuggestedSuffix(String suggestedSuffix) {
    this.suggestedSuffix = suggestedSuffix;
  }

  public void setMaxLength(Integer maxLength) {
    this.maxLength = maxLength;
  }

  public void setRegex(String regex) {
    this.regex = regex;
  }
}
