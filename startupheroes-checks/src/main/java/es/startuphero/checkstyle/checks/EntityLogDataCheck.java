package es.startuphero.checkstyle.checks;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import es.startuphero.checkstyle.util.AnnotationUtil;
import es.startuphero.checkstyle.util.ClassUtil;
import es.startuphero.checkstyle.util.VariableUtil;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import static java.util.Objects.nonNull;

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

   private String abstractEntityAnnotation;

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
      if (ClassUtil.isEntity(ast, entityAnnotation) || ClassUtil.isEntity(ast, abstractEntityAnnotation)) {
         String className = ClassUtil.getClassName(ast);
         if (className.endsWith(LOG_ENTITY_SUFFIX)) {
            List<DetailAST> variableNodes = VariableUtil.getNonStaticVariables(ast);
            variableNodes.forEach(this::checkVariableLength);
         }
      }
   }

   private void assertions() {
      Assert.isTrue(!StringUtils.isEmpty(entityAnnotation));
      Assert.isTrue(!StringUtils.isEmpty(abstractEntityAnnotation));
      Assert.isTrue(!StringUtils.isEmpty(columnAnnotation));
      Assert.isTrue(!StringUtils.isEmpty(limitLength));
   }

   private void checkVariableLength(DetailAST variableNode) {
      DetailAST columnAnnotationNode = AnnotationUtil.getAnnotation(variableNode, columnAnnotation);
      if (nonNull(columnAnnotationNode)) {
         Map<String, DetailAST> keyValueAstMap = AnnotationUtil.getKeyValueAstMap(columnAnnotationNode);
         DetailAST lengthKeyValueNode = keyValueAstMap.get("length");
         if (nonNull(lengthKeyValueNode)) {
            Optional<String> valueAsString = AnnotationUtil.getValueAsString(lengthKeyValueNode);
            if (valueAsString.isPresent()) {
               Integer variableLength = Integer.valueOf(valueAsString.get());
               if (variableLength > limitLength) {
                  log(variableNode.getLineNo(), MSG_KEY, VariableUtil.getVariableName(variableNode));
               }
            }
         }
      }
   }

   public void setEntityAnnotation(String entityAnnotation) {
      this.entityAnnotation = entityAnnotation;
   }

   public void setAbstractEntityAnnotation(String abstractEntityAnnotation) {
      this.abstractEntityAnnotation = abstractEntityAnnotation;
   }

   public void setColumnAnnotation(String columnAnnotation) {
      this.columnAnnotation = columnAnnotation;
   }

   public void setLimitLength(Integer limitLength) {
      this.limitLength = limitLength;
   }

}
