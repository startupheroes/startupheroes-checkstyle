package es.startupheroes.checkstyle.checks;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import es.startupheroes.checkstyle.util.AnnotationUtil;
import es.startupheroes.checkstyle.util.ClassUtil;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import static java.util.Objects.nonNull;
import static es.startupheroes.checkstyle.util.CommonUtil.convertToDatabaseForm;
import static es.startupheroes.checkstyle.util.CommonUtil.getDatabaseIdentifierName;

/**
 * @author ozlem.ulag
 */
public class EntityUniqueConstraintNameCheck extends AbstractCheck {

   /**
    * A key is pointing to the warning message text in "messages.properties" file.
    */
   private static final String MSG_KEY = "entityUniqueConstraintNameCheckMessage";

   private static final String MSG_IDENTIFIER_NAME_TOO_LONG = "databaseIdentifierNameTooLongMessage";

   /**
    * set entity annotation to understand that a class is an entity.
    */
   private String entityAnnotation;

   private String abstractEntityAnnotation;

   private String tableAnnotation;

   private String uniqueConstraintAnnotation;

   private String key;

   private String keyName;

   private String keyColumns;

   /**
    * set convenient suffix to add to the end of the unique constraint name.
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
      if ((ClassUtil.isEntity(ast, entityAnnotation) || ClassUtil.isEntity(ast, abstractEntityAnnotation)) && AnnotationUtil.hasAnnotation(ast, tableAnnotation)) {
         Map<String, DetailAST> tableKeyValueAstMap = AnnotationUtil.getKeyValueAstMap(AnnotationUtil.getAnnotation(ast, tableAnnotation));
         if (tableKeyValueAstMap.containsKey(key)) {
            DetailAST keyValuePairNode = tableKeyValueAstMap.get(key);
            List<DetailAST> uniqueConstraintAnnotationNodes = AnnotationUtil.getValueAsAnnotations(keyValuePairNode);
            String className = ClassUtil.getClassName(ast);
            uniqueConstraintAnnotationNodes.forEach(uniqueConstraintAnnotationNode ->
                                                        checkUniqueConstraintName(className, uniqueConstraintAnnotationNode));
         }
      }
   }

   private void checkUniqueConstraintName(String className, DetailAST uniqueConstraintAnnotationNode) {
      Map<String, DetailAST> uniqueConstraintKeyValueAstMap = AnnotationUtil.getKeyValueAstMap(uniqueConstraintAnnotationNode);
      DetailAST nameKeyValueAst = uniqueConstraintKeyValueAstMap.get(keyName);
      Optional<String> ucNameOptional = nonNull(nameKeyValueAst) ? AnnotationUtil.getValueAsString(nameKeyValueAst) : Optional.empty();
      String ucName = ucNameOptional.isPresent() ? ucNameOptional.get() : "";
      checkBySuggestedName(className, uniqueConstraintAnnotationNode, uniqueConstraintKeyValueAstMap, ucName);
   }

   private void checkBySuggestedName(String className, DetailAST uniqueConstraintAnnotationNode,
                                     Map<String, DetailAST> uniqueConstraintKeyValueAstMap,
                                     String uniqueConstraintName) {
      DetailAST columnsKeyValueAst = uniqueConstraintKeyValueAstMap.get(keyColumns);
      if (nonNull(columnsKeyValueAst)) {
         List<String> columnNames = AnnotationUtil.getValueAsStringList(columnsKeyValueAst);
         if (!columnNames.isEmpty()) {
            String suggestedUniqueConstraintName = getSuggestedUniqueConstraintName(className, columnNames);
            if (uniqueConstraintName.length() > maxLength) {
               log(uniqueConstraintAnnotationNode.getLineNo(), MSG_IDENTIFIER_NAME_TOO_LONG, uniqueConstraintName, maxLength);
            } else if (suggestedUniqueConstraintName.length() <= maxLength &&
                       !suggestedUniqueConstraintName.equals(uniqueConstraintName)) {
               log(uniqueConstraintAnnotationNode.getLineNo(), MSG_KEY, suggestedUniqueConstraintName);
            } else if (!acceptableUniqueConstraintName(className, uniqueConstraintName)) {
               log(uniqueConstraintAnnotationNode.getLineNo(), MSG_KEY,
                   getSuggestedUniqueConstraintName(className, Collections.singletonList("your_fields")));
            }
         }
      }
   }

   private String getSuggestedUniqueConstraintName(String className, List<String> columnNames) {
      return convertToDatabaseForm(className, suggestedSuffix, columnNames);
   }

   private Boolean acceptableUniqueConstraintName(String tableName, String indexName) {
      return indexName.startsWith(getDatabaseIdentifierName(tableName + "_")) &&
             Pattern.compile(regex).matcher(indexName).matches() &&
             indexName.endsWith(getDatabaseIdentifierName("_" + suggestedSuffix));
   }

   private void assertions() {
      Assert.isTrue(!StringUtils.isEmpty(entityAnnotation));
      Assert.isTrue(!StringUtils.isEmpty(abstractEntityAnnotation));
      Assert.isTrue(!StringUtils.isEmpty(tableAnnotation));
      Assert.isTrue(!StringUtils.isEmpty(uniqueConstraintAnnotation));
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

   public void setAbstractEntityAnnotation(String abstractEntityAnnotation) {
      this.abstractEntityAnnotation = abstractEntityAnnotation;
   }

   public void setTableAnnotation(String tableAnnotation) {
      this.tableAnnotation = tableAnnotation;
   }

   public void setUniqueConstraintAnnotation(String uniqueConstraintAnnotation) {
      this.uniqueConstraintAnnotation = uniqueConstraintAnnotation;
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
