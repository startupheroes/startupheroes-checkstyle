package es.startupheroes.checkstyle.checks;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static java.util.Objects.nonNull;
import static es.startupheroes.checkstyle.util.AnnotationUtil.getKeyDefaultValueMap;
import static es.startupheroes.checkstyle.util.AnnotationUtil.getKeyValueAstMap;
import static es.startupheroes.checkstyle.util.AnnotationUtil.getValueAsString;
import static es.startupheroes.checkstyle.util.ClassUtil.getImportSimpleFullNameMap;
import static es.startupheroes.checkstyle.util.CommonUtil.getFullName;
import static es.startupheroes.checkstyle.util.CommonUtil.getSimpleName;

/**
 * @author ozlem.ulag
 */
public class RedundantDefaultAnnotationParameterAssignCheck extends AbstractCheck {

   /**
    * A key is pointing to the warning message text in "messages.properties" file.
    */
   private static final String MSG_KEY = "redundantDefaultAnnotationParameterAssignCheckMessage";

   private Map<String, String> importSimpleFullNameMap;

   @Override
   public int[] getDefaultTokens() {
      return getAcceptableTokens();
   }

   @Override
   public int[] getAcceptableTokens() {
      return new int[]{TokenTypes.ANNOTATION};
   }

   @Override
   public int[] getRequiredTokens() {
      return getAcceptableTokens();
   }

   @Override
   public void beginTree(DetailAST rootAST) {
      importSimpleFullNameMap = getImportSimpleFullNameMap(rootAST);
   }

   @Override
   public void visitToken(DetailAST ast) {
      Map<String, DetailAST> annotationKeyValueAstMap = getKeyValueAstMap(ast);
      if (!annotationKeyValueAstMap.isEmpty()) {
         String annotationSimpleName = getSimpleName(ast);
         String fullAnnotationName = getFullName(ast, importSimpleFullNameMap, annotationSimpleName);
         Set<String> keys = annotationKeyValueAstMap.keySet();
         Map<String, Object> keyDefaultValueMap = getKeyDefaultValueMap(fullAnnotationName, keys);
         for (String key : keys) {
            Optional<String> value = getValueAsString(annotationKeyValueAstMap.get(key));
            if (value.isPresent()) {
               Object defaultValue = keyDefaultValueMap.get(key);
               if (nonNull(defaultValue) && value.get().equals(defaultValue.toString())) {
                  log(ast.getLineNo(), MSG_KEY, key);
               }
            }
         }
      }
   }

}
