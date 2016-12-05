package es.startuphero.checkstyle.checks.naming;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import es.startuphero.checkstyle.util.AnnotationUtil;
import es.startuphero.checkstyle.util.CommonUtil;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import static es.startuphero.checkstyle.util.AnnotationUtil.getKeyValueAstMap;
import static es.startuphero.checkstyle.util.AnnotationUtil.getValueAsAnnotations;
import static es.startuphero.checkstyle.util.AnnotationUtil.getValueAsString;
import static es.startuphero.checkstyle.util.AnnotationUtil.hasAnnotation;
import static es.startuphero.checkstyle.util.ClassUtil.getClassName;
import static es.startuphero.checkstyle.util.CommonUtil.getSplitterOnComma;
import static java.util.Objects.nonNull;

/**
 * @author ozlem.ulag
 */
public class TableIdentifierNameCheck extends AbstractCheck {

  /**
   * A key is pointing to the warning message text in "messages.properties" file.
   */
  private static final String MSG_KEY = "illegal.table.identifier.name";

  private static final String MSG_IDENTIFIER_NAME_TOO_LONG = "table.identifier.name.too.long";

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
    if (hasAnnotation(ast, tableAnnotation)) {
      Map<String, DetailAST> tableKeyValueAstMap =
          getKeyValueAstMap(AnnotationUtil.getAnnotation(ast, tableAnnotation));
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
    Optional<String> identifierNameOptional =
        nonNull(nameKeyValueAst) ? getValueAsString(nameKeyValueAst) : Optional.empty();
    String identifierName = identifierNameOptional.isPresent() ? identifierNameOptional.get() : "";
    checkBySuggestedName(className, identifierAnnotationNode, identifierKeyValueAstMap, identifierName);
  }

  private void checkBySuggestedName(String className, DetailAST identifierAnnotationNode,
                                    Map<String, DetailAST> identifierKeyValueAstMap, String identifierName) {
    DetailAST columnsKeyValueAst = identifierKeyValueAstMap.get(keyColumns);
    if (nonNull(columnsKeyValueAst)) {
      List<String> columnNames = AnnotationUtil.getValueAsStringList(columnsKeyValueAst);
      if (!columnNames.isEmpty()) {
        String suggestedIdentifierName = getSuggestedIdentifierName(className, columnNames);
        if (identifierName.length() > maxLength) {
          log(identifierAnnotationNode.getLineNo(), MSG_IDENTIFIER_NAME_TOO_LONG, identifierName, maxLength);
        } else if (suggestedIdentifierName.length() <= maxLength &&
                   !suggestedIdentifierName.equals(identifierName)) {
          log(identifierAnnotationNode.getLineNo(), MSG_KEY, suggestedIdentifierName);
        } else if (!acceptableIdentifierName(className, identifierName)) {
          log(identifierAnnotationNode.getLineNo(), MSG_KEY,
              getSuggestedIdentifierName(className, Collections.singletonList("your_fields")));
        }
      }
    }
  }

  private String getSuggestedIdentifierName(String className, List<String> columnNames) {
    return CommonUtil.convertToDatabaseForm(className, suggestedSuffix,
                                            columnNames.size() == 1 ? getSplitterOnComma().split(columnNames.get(0))
                                                : columnNames);
  }

  private Boolean acceptableIdentifierName(String tableName, String identifierName) {
    return identifierName.startsWith(CommonUtil.getDatabaseIdentifierName(tableName + "_")) &&
           Pattern.compile(regex).matcher(identifierName).matches() &&
           identifierName.endsWith(CommonUtil.getDatabaseIdentifierName("_" + suggestedSuffix));
  }

  private void assertions() {
    Assert.isTrue(!StringUtils.isEmpty(tableAnnotation));
    Assert.isTrue(!StringUtils.isEmpty(identifierAnnotation));
    Assert.isTrue(!StringUtils.isEmpty(key));
    Assert.isTrue(!StringUtils.isEmpty(keyName));
    Assert.isTrue(!StringUtils.isEmpty(keyColumns));
    Assert.isTrue(!StringUtils.isEmpty(suggestedSuffix));
    Assert.isTrue(!StringUtils.isEmpty(maxLength));
    Assert.isTrue(!StringUtils.isEmpty(regex));
  }
}
