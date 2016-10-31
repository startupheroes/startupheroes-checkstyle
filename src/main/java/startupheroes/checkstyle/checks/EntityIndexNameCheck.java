package startupheroes.checkstyle.checks;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import static java.util.Objects.nonNull;
import static startupheroes.checkstyle.util.AnnotationUtil.getAnnotation;
import static startupheroes.checkstyle.util.AnnotationUtil.getKeyValueAstMap;
import static startupheroes.checkstyle.util.AnnotationUtil.getValueAsAnnotations;
import static startupheroes.checkstyle.util.AnnotationUtil.getValueAsString;
import static startupheroes.checkstyle.util.AnnotationUtil.hasAnnotation;
import static startupheroes.checkstyle.util.ClassUtil.getClassName;
import static startupheroes.checkstyle.util.ClassUtil.isEntity;
import static startupheroes.checkstyle.util.CommonUtil.convertToDatabaseForm;
import static startupheroes.checkstyle.util.CommonUtil.getDatabaseIdentifierName;
import static startupheroes.checkstyle.util.CommonUtil.getSplitterOnComma;

/**
 * @author ozlem.ulag
 */
public class EntityIndexNameCheck extends AbstractCheck {

   /**
    * A key is pointing to the warning message text in "messages.properties" file.
    */
   private static final String MSG_KEY = "entityIndexNameCheckMessage";

   private static final String MSG_IDENTIFIER_NAME_TOO_LONG = "databaseIdentifierNameTooLongMessage";

   /**
    * set entity annotation to understand that a class is an entity.
    */
   private String entityAnnotation;

   private String tableAnnotation;

   private String indexAnnotation;

   private String key;

   private String keyName;

   private String keyColumns;

   /**
    * set convenient suffix to add to the end of the index name.
    */
   private String suggestedSuffix;

   private Integer maxLength;

   private String regex;

   @Override
   public int[] getDefaultTokens() {
      return getAcceptableTokens();
   }

   @Override
   public int[] getAcceptableTokens() {
      return new int[]{TokenTypes.CLASS_DEF};
   }

   @Override
   public int[] getRequiredTokens() {
      return getAcceptableTokens();
   }

   @Override
   public void visitToken(DetailAST ast) {
      assertions();
      if (isEntity(ast, entityAnnotation) && hasAnnotation(ast, tableAnnotation)) {
         Map<String, DetailAST> tableKeyValueAstMap = getKeyValueAstMap(getAnnotation(ast, tableAnnotation));
         if (tableKeyValueAstMap.containsKey(key)) {
            DetailAST keyValuePairNode = tableKeyValueAstMap.get(key);
            List<DetailAST> indexAnnotationNodes = getValueAsAnnotations(keyValuePairNode);
            String className = getClassName(ast);
            indexAnnotationNodes.forEach(indexAnnotationNode -> checkIndexName(className, indexAnnotationNode));
         }
      }
   }

   private void checkIndexName(String className, DetailAST indexAnnotationNode) {
      Map<String, DetailAST> indexKeyValueAstMap = getKeyValueAstMap(indexAnnotationNode);
      DetailAST nameKeyValueAst = indexKeyValueAstMap.get(keyName);
      if (nonNull(nameKeyValueAst)) {
         Optional<String> indexNameOptional = getValueAsString(nameKeyValueAst);
         DetailAST columnsKeyValueAst = indexKeyValueAstMap.get(keyColumns);
         if (nonNull(columnsKeyValueAst)) {
            Optional<String> columnList = getValueAsString(columnsKeyValueAst);
            if (indexNameOptional.isPresent() && columnList.isPresent()) {
               String indexName = indexNameOptional.get();
               String suggestedIndexName = getSuggestedIndexName(className, columnList.get());
               if (indexName.length() > maxLength) {
                  log(indexAnnotationNode.getLineNo(), MSG_IDENTIFIER_NAME_TOO_LONG, indexName, maxLength);
               } else if (suggestedIndexName.length() <= maxLength && !suggestedIndexName.equals(indexName)) {
                  log(indexAnnotationNode.getLineNo(), MSG_KEY, suggestedIndexName);
               } else if (!acceptableIndexName(className, indexName)) {
                  log(indexAnnotationNode.getLineNo(), MSG_KEY, getSuggestedIndexName(className, "your_fields"));
               }
            }
         }
      } else {
         log(indexAnnotationNode.getLineNo(), MSG_KEY, getSuggestedIndexName(className, "your_fields"));
      }
   }

   private String getSuggestedIndexName(String className, String columnList) {
      return convertToDatabaseForm(className,
                                   suggestedSuffix,
                                   getSplitterOnComma().split(columnList));
   }

   private Boolean acceptableIndexName(String tableName, String indexName) {
      return indexName.startsWith(getDatabaseIdentifierName(tableName + "_")) &&
             Pattern.compile(regex).matcher(indexName).matches() &&
             indexName.endsWith(getDatabaseIdentifierName("_" + suggestedSuffix));
   }

   private void assertions() {
      Assert.isTrue(!StringUtils.isEmpty(entityAnnotation));
      Assert.isTrue(!StringUtils.isEmpty(tableAnnotation));
      Assert.isTrue(!StringUtils.isEmpty(indexAnnotation));
      Assert.isTrue(!StringUtils.isEmpty(key));
      Assert.isTrue(!StringUtils.isEmpty(keyName));
      Assert.isTrue(!StringUtils.isEmpty(keyColumns));
      Assert.isTrue(!StringUtils.isEmpty(suggestedSuffix));
      Assert.isTrue(!StringUtils.isEmpty(maxLength));
      Assert.isTrue(!StringUtils.isEmpty(regex));
   }

   public void setEntityAnnotation(String entityAnnotation) {
      this.entityAnnotation = entityAnnotation;
   }

   public void setTableAnnotation(String tableAnnotation) {
      this.tableAnnotation = tableAnnotation;
   }

   public void setIndexAnnotation(String indexAnnotation) {
      this.indexAnnotation = indexAnnotation;
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
