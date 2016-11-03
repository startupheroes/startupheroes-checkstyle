package startupheroes.checkstyle.checks;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import static java.util.Objects.nonNull;
import static startupheroes.checkstyle.util.AnnotationUtil.getAnnotation;
import static startupheroes.checkstyle.util.AnnotationUtil.getKeyValueAstMap;
import static startupheroes.checkstyle.util.AnnotationUtil.getValueAsString;
import static startupheroes.checkstyle.util.ClassUtil.getClassName;
import static startupheroes.checkstyle.util.ClassUtil.isEntity;
import static startupheroes.checkstyle.util.VariableUtil.getNonStaticVariables;
import static startupheroes.checkstyle.util.VariableUtil.getVariableName;
import static startupheroes.checkstyle.util.VariableUtil.getVariables;

/**
 * @author ozlem.ulag
 */
public class EntityLogDataCheck extends AbstractCheck {

   /**
    * A key is pointing to the warning message text in "messages.properties" file.
    */
   private static final String MSG_KEY = "entityLogDataCheckMessage";

   private static final String LOG_ENTITY_SUFFIX = "Log";

   /**
    * set entity annotation to understand that a class is an entity.
    */
   private String entityAnnotation;

   private String columnAnnotation;

   private Integer limitLength;

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
      if (isEntity(ast, entityAnnotation)) {
         String className = getClassName(ast);
         if (className.endsWith(LOG_ENTITY_SUFFIX)) {
            List<DetailAST> variableNodes = getNonStaticVariables(ast);
            variableNodes.forEach(this::checkVariableLength);
         }
      }
   }

   private void assertions() {
      Assert.isTrue(!StringUtils.isEmpty(entityAnnotation));
      Assert.isTrue(!StringUtils.isEmpty(columnAnnotation));
      Assert.isTrue(!StringUtils.isEmpty(limitLength));
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
                  log(variableNode.getLineNo(), MSG_KEY, getVariableName(variableNode));
               }
            }
         }
      }
   }

   public void setEntityAnnotation(String entityAnnotation) {
      this.entityAnnotation = entityAnnotation;
   }

   public void setColumnAnnotation(String columnAnnotation) {
      this.columnAnnotation = columnAnnotation;
   }

   public void setLimitLength(Integer limitLength) {
      this.limitLength = limitLength;
   }

}
