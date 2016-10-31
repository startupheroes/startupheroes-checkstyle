package startupheroes.checkstyle.checks;

import com.google.common.collect.Iterables;
import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import static startupheroes.checkstyle.util.AnnotationUtil.getAnnotation;
import static startupheroes.checkstyle.util.AnnotationUtil.getKeyValueAstMap;
import static startupheroes.checkstyle.util.AnnotationUtil.getValueAsAnnotations;
import static startupheroes.checkstyle.util.AnnotationUtil.getValueAsString;
import static startupheroes.checkstyle.util.AnnotationUtil.hasAnnotation;
import static startupheroes.checkstyle.util.ClassUtil.getClassName;
import static startupheroes.checkstyle.util.ClassUtil.isEntity;
import static startupheroes.checkstyle.util.CommonUtil.convertToDatabaseForm;
import static startupheroes.checkstyle.util.CommonUtil.getSplitterOnComma;

/**
 * @author ozlem.ulag
 */
public class EntityIndexNameCheck extends AbstractCheck {

   /**
    * A key is pointing to the warning message text in "messages.properties" file.
    */
   private static final String MSG_KEY = "entityIndexNameCheckMessage";

   private static final String INDEX_KEY = "indexes";

   private static final String INDEX_KEY_NAME = "name";

   private static final String INDEX_KEY_COLUMN_LIST = "columnList";

   /**
    * set entity annotation to understand that a class is an entity.
    */
   private String entityAnnotation;

   private String tableAnnotation;

   private String indexAnnotation;

   /**
    * set convenient suffix to add to the end of the index name.
    */
   private String suggestedIndexSuffix;

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
         if (tableKeyValueAstMap.containsKey(INDEX_KEY)) {
            DetailAST indexKeyValuePairNode = tableKeyValueAstMap.get(INDEX_KEY);
            List<DetailAST> indexAnnotationNodes = getValueAsAnnotations(indexKeyValuePairNode);
            String className = getClassName(ast);
            indexAnnotationNodes.forEach(indexAnnotationNode -> checkIndexName(className, indexAnnotationNode));
         }
      }
   }

   private void checkIndexName(String className, DetailAST indexAnnotationNode) {
      Map<String, DetailAST> indexKeyValueAstMap = getKeyValueAstMap(indexAnnotationNode);
      Optional<String> indexName = getValueAsString(indexKeyValueAstMap.get(INDEX_KEY_NAME));
      Optional<String> columnList = getValueAsString(indexKeyValueAstMap.get(INDEX_KEY_COLUMN_LIST));
      if (indexName.isPresent() && columnList.isPresent()) {
         String suggestedIndexName = getSuggestedIndexName(className, columnList.get());
         if (!suggestedIndexName.equals(indexName.get())) {
            log(indexAnnotationNode.getLineNo(), MSG_KEY, suggestedIndexName);
         }
      }
   }

   private String getSuggestedIndexName(String className, String columnList) {
      return convertToDatabaseForm(className,
                                   suggestedIndexSuffix,
                                   Iterables.toArray(getSplitterOnComma().split(columnList), String.class));
   }

   private void assertions() {
      Assert.isTrue(!StringUtils.isEmpty(entityAnnotation));
      Assert.isTrue(!StringUtils.isEmpty(tableAnnotation));
      Assert.isTrue(!StringUtils.isEmpty(indexAnnotation));
      Assert.isTrue(!StringUtils.isEmpty(suggestedIndexSuffix));
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

   public void setSuggestedIndexSuffix(String suggestedIndexSuffix) {
      this.suggestedIndexSuffix = suggestedIndexSuffix;
   }

}
