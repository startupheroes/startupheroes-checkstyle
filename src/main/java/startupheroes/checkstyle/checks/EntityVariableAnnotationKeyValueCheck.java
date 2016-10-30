package startupheroes.checkstyle.checks;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static startupheroes.checkstyle.util.AnnotationUtil.getAnnotation;
import static startupheroes.checkstyle.util.AnnotationUtil.getKeyValueAstMap;
import static startupheroes.checkstyle.util.AnnotationUtil.getValue;
import static startupheroes.checkstyle.util.ClassUtil.isEntity;
import static startupheroes.checkstyle.util.CommonUtil.getSimpleName;
import static startupheroes.checkstyle.util.VariableUtil.getVariableNameAstMap;

/**
 * @author ozlem.ulag
 */
public class EntityVariableAnnotationKeyValueCheck extends AbstractCheck {

   /**
    * A key is pointing to the warning message text in "messages.properties" file.
    */
   private static final String MSG_KEY = "entityAnnotationKeyValueCheckMessage";

   /**
    * set entity annotation to understand that a class is an entity.
    */
   private String entityAnnotation;

   private Table<String, String, Map<String, String>> variableAnnotationKeyValueTable = HashBasedTable.create();

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
      Assert.isTrue(!StringUtils.isEmpty(entityAnnotation));
      if (isEntity(ast, entityAnnotation)) {
         Map<String, DetailAST> variableNameAstMap = getVariableNameAstMap(ast);
         Set<String> checkedVariables = variableAnnotationKeyValueTable.rowKeySet();
         Set<String> checkedAnnotations = variableAnnotationKeyValueTable.columnKeySet();
         checkedVariables.stream()
                         .filter(checkedVariable -> variableNameAstMap.keySet().contains(checkedVariable))
                         .forEach(checkedVariable -> {
                            DetailAST variableAst = variableNameAstMap.get(checkedVariable);
                            checkedAnnotations.forEach(checkedAnnotation -> checkAnnotation(checkedVariable,
                                                                                            variableAst,
                                                                                            checkedAnnotation));
                         });
      }
   }

   private void checkAnnotation(String checkedVariable, DetailAST variableAst, String checkedAnnotation) {
      DetailAST annotationAst = getAnnotation(variableAst, checkedAnnotation);
      if (nonNull(annotationAst)) {
         Map<String, DetailAST> annotationKeyPairAstMap = getKeyValueAstMap(annotationAst);
         Map<String, String> checkedKeyValueMap = variableAnnotationKeyValueTable.get(checkedVariable, checkedAnnotation);
         checkedKeyValueMap.keySet().forEach(checkedKey -> checkKeyValuePair(checkedVariable,
                                                                             checkedAnnotation,
                                                                             annotationAst,
                                                                             annotationKeyPairAstMap,
                                                                             checkedKeyValueMap,
                                                                             checkedKey));
      }
   }

   private void checkKeyValuePair(String checkedVariable, String checkedAnnotation, DetailAST annotationAst,
                                  Map<String, DetailAST> annotationKeyPairAstMap, Map<String, String> checkedKeyValueMap,
                                  String checkedKey) {
      String checkedValue = checkedKeyValueMap.get(checkedKey);
      DetailAST annotationKeyValueAst = annotationKeyPairAstMap.get(checkedKey);
      if (nonNull(annotationKeyValueAst)) {
         Optional<String> annotationValueAsString = getValue(annotationKeyValueAst);
         if (annotationValueAsString.isPresent() && !annotationValueAsString.get().equals(checkedValue)) {
            log(annotationAst.getLineNo(), MSG_KEY, checkedVariable, getSimpleName(checkedAnnotation), checkedKey, checkedValue);
         }
      } else {
         log(annotationAst.getLineNo(), MSG_KEY, checkedVariable, getSimpleName(checkedAnnotation), checkedKey, checkedValue);
      }
   }

   public void setEntityAnnotation(String entityAnnotation) {
      this.entityAnnotation = entityAnnotation;
   }

   public void setVariableAnnotationKeyValueTable(String... variableAnnotationKeyValues) {
      for (String variableAnnotationKeyValue : variableAnnotationKeyValues) {
         String[] separated = variableAnnotationKeyValue.split(":");
         String variable = separated[0];
         String annotation = separated[1];
         String key = separated[2];
         String value = separated[3];
         Map<String, String> keyValueMap = variableAnnotationKeyValueTable.get(variable, annotation);
         if (isNull(keyValueMap)) {
            keyValueMap = new HashMap<>();
         }
         keyValueMap.put(key, value);
         variableAnnotationKeyValueTable.put(variable, annotation, keyValueMap);
      }
   }

}
