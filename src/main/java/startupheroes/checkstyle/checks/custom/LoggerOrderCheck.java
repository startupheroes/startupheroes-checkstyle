package startupheroes.checkstyle.checks.custom;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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
      DetailAST objBlock = ast.findFirstToken(TokenTypes.OBJBLOCK);

      Map<DetailAST, String> astClassVariableMap = new LinkedHashMap<>();

      for (DetailAST child = objBlock.getFirstChild(); child != null; child = child.getNextSibling()) {
         if (child.getType() == TokenTypes.VARIABLE_DEF) {
            String variableName = child.findFirstToken(TokenTypes.IDENT).getText();
            astClassVariableMap.put(child, variableName);
         }
      }

      List<String> classVariables = astClassVariableMap.values().stream().collect(Collectors.toList());
      if (!classVariables.isEmpty() && isLoggerInCorrectOrder(classVariables)) {
         reportStyleError(getASTHasLoggerVariable(astClassVariableMap));
      }
   }

   private DetailAST getASTHasLoggerVariable(Map<DetailAST, String> astClassVariableMap) {
      return astClassVariableMap.entrySet()
                                .stream()
                                .filter(entry -> Objects.equals(entry.getValue(), VARIABLE_LOGGER))
                                .map(Map.Entry::getKey)
                                .collect(Collectors.toSet()).iterator().next();
   }

   private Boolean isLoggerInCorrectOrder(List<String> classVariables) {
      return classVariables.contains(VARIABLE_LOGGER) && classVariables.indexOf(VARIABLE_LOGGER) != 0;
   }

   private void reportStyleError(DetailAST aAST) {
      log(aAST.getLineNo(), MSG_KEY);
   }

}
