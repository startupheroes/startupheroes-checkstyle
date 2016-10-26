package startupheroes.checkstyle.checks.custom;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import startupheroes.checkstyle.util.CommonUtil;

/**
 * @author ozlem.ulag
 */
public class LoggerOrderCheck extends AbstractCheck {

   private static final String VARIABLE_LOGGER = "LOGGER";

   /**
    * A key is pointing to the warning message text in "messages.properties"
    * file.
    */
   static final String MSG_KEY = "loggerOrderCheckMessage";

   @Override
   public int[] getDefaultTokens() {
      return new int[]{TokenTypes.CLASS_DEF, TokenTypes.INTERFACE_DEF};
   }

   @Override
   public void visitToken(DetailAST ast) {
      Map<DetailAST, String> variableAstNameMap = getOrderedVariableAstNameMap(ast);
      if (!variableAstNameMap.isEmpty() && isLoggerInCorrectOrder(variableAstNameMap)) {
         log(getLoggerAst(variableAstNameMap).getLineNo(), MSG_KEY);
      }
   }

   private static LinkedHashMap<DetailAST, String> getOrderedVariableAstNameMap(DetailAST ast) {
      List<DetailAST> variables = CommonUtil.getClassVariables(ast);
      return variables.stream().collect(Collectors.toMap(Function.identity(),
                                                         vAst -> vAst.findFirstToken(TokenTypes.IDENT).getText(),
                                                         (v1, v2) -> null,
                                                         LinkedHashMap::new));
   }

   private static DetailAST getLoggerAst(Map<DetailAST, String> astVariableMap) {
      return astVariableMap.entrySet()
                           .stream()
                           .filter(entry -> Objects.equals(entry.getValue(), VARIABLE_LOGGER))
                           .map(Map.Entry::getKey)
                           .collect(Collectors.toSet()).iterator().next();
   }

   private static Boolean isLoggerInCorrectOrder(Map<DetailAST, String> astVariableMap) {
      List<String> variableNames = astVariableMap.values().stream().collect(Collectors.toList());
      return variableNames.contains(VARIABLE_LOGGER) && variableNames.indexOf(VARIABLE_LOGGER) != 0;
   }

}
