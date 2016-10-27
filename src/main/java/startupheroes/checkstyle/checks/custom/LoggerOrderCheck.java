package startupheroes.checkstyle.checks.custom;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static startupheroes.checkstyle.util.VariableUtil.getOrderedVariableNameAstMap;

/**
 * @author ozlem.ulag
 */
public class LoggerOrderCheck extends AbstractCheck {

   /**
    * A key is pointing to the warning message text in "messages.properties" file.
    */
   private static final String MSG_KEY = "loggerOrderCheckMessage";

   private static final String VARIABLE_LOGGER = "LOGGER";

   @Override
   public int[] getDefaultTokens() {
      return new int[]{TokenTypes.CLASS_DEF, TokenTypes.INTERFACE_DEF};
   }

   @Override
   public void visitToken(DetailAST ast) {
      Map<String, DetailAST> variableNameAstMap = getOrderedVariableNameAstMap(ast);
      if (variableNameAstMap.containsKey(VARIABLE_LOGGER) && isLoggerInCorrectOrder(variableNameAstMap)) {
         log(variableNameAstMap.get(VARIABLE_LOGGER).getLineNo(), MSG_KEY);
      }
   }

   private static Boolean isLoggerInCorrectOrder(Map<String, DetailAST> variableNameAstMap) {
      List<String> variableNames = variableNameAstMap.keySet().stream().collect(Collectors.toList());
      return variableNames.indexOf(VARIABLE_LOGGER) != 0;
   }

}
