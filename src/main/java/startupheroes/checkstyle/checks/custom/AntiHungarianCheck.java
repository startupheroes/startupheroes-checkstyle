package startupheroes.checkstyle.checks.custom;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import java.util.regex.Pattern;

import static startupheroes.checkstyle.util.CommonUtil.getVariableName;

public class AntiHungarianCheck extends AbstractCheck {

   /**
    * A key is pointing to the warning message text in "messages.properties"
    * file.
    */
   private static final String MSG_KEY = "antiHungarianCheckMessage";

   private final HungarianNotationMemberDetector detector = new HungarianNotationMemberDetector();

   @Override
   public int[] getDefaultTokens() {
      return new int[]{TokenTypes.VARIABLE_DEF};
   }

   @Override
   public void visitToken(DetailAST ast) {
      String variableName = getVariableName(ast);
      if (isFieldVariable(ast) && detector.detectsNotation(variableName)) {
         log(ast.getLineNo(), MSG_KEY + variableName);
      }
   }

   private Boolean isFieldVariable(DetailAST ast) {
      return ast.getParent().getType() == TokenTypes.OBJBLOCK;
   }

   private static final class HungarianNotationMemberDetector {

      private Pattern pattern = Pattern.compile("m[A-Z0-9].*");

      Boolean detectsNotation(String variableName) {
         return pattern.matcher(variableName).matches();
      }

   }

}
