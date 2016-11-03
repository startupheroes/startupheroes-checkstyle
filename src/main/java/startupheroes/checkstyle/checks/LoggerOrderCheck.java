package startupheroes.checkstyle.checks;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.Scope;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static startupheroes.checkstyle.util.VariableUtil.getScopeOf;
import static startupheroes.checkstyle.util.VariableUtil.getVariableNameAstMap;

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
      Map<String, DetailAST> variableNameAstMap = getVariableNameAstMap(ast);
      if (variableNameAstMap.containsKey(VARIABLE_LOGGER)) {
         DetailAST loggerAst = variableNameAstMap.get(VARIABLE_LOGGER);
         Scope scopeOfLogger = getScopeOf(loggerAst);
         variableNameAstMap = getVariableNameAstMap(ast, scopeOfLogger);
         if (isLoggerNotInCorrectOrder(variableNameAstMap)) {
            log(loggerAst.getLineNo(), MSG_KEY);
         }
      }
   }

   private static Boolean isLoggerNotInCorrectOrder(Map<String, DetailAST> variableNameAstMap) {
      List<String> variableNames = variableNameAstMap.keySet().stream().collect(Collectors.toList());
      return variableNames.indexOf(VARIABLE_LOGGER) != 0;
   }

}
