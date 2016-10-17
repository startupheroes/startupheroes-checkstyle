package startupheroes.checks;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ozlem.ulag
 */
public class LoggerOrderCheck extends AbstractCheck {

   private static final String VARIABLE_LOGGER = "LOGGER";

   private static final String CATCH_MSG = VARIABLE_LOGGER + " definitions should be at the top of the class.";

   @Override
   public int[] getDefaultTokens() {
      return new int[]{TokenTypes.CLASS_DEF, TokenTypes.INTERFACE_DEF};
   }

   @Override
   public void visitToken(DetailAST ast) {
      DetailAST objBlock = ast.findFirstToken(TokenTypes.OBJBLOCK);
      List<String> classVariables = new ArrayList<>();

      for (DetailAST child = objBlock.getFirstChild(); child != null; child = child.getNextSibling()) {
         if (child.getType() == TokenTypes.VARIABLE_DEF) {
            classVariables.add(child.findFirstToken(TokenTypes.IDENT).getText());
         }
      }
      if (!classVariables.isEmpty() && isLoggerInCorrectOrder(classVariables)) {
         reportStyleError(ast);
      }
   }

   private Boolean isLoggerInCorrectOrder(List<String> classVariables) {
      return classVariables.contains(VARIABLE_LOGGER) && classVariables.indexOf(VARIABLE_LOGGER) != 0;
   }

   private void reportStyleError(DetailAST aAST) {
      log(aAST.getLineNo(), CATCH_MSG);
   }

}
