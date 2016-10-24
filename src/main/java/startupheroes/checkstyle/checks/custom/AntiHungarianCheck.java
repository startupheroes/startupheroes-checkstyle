package startupheroes.checkstyle.checks.custom;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import java.util.regex.Pattern;

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
   public void visitToken(DetailAST aAST) {
      String variableName = findVariableName(aAST);
      if (itsAFieldVariable(aAST) && detector.detectsNotation(variableName)) {
         reportStyleError(aAST, variableName);
      }
   }

   private String findVariableName(DetailAST aAST) {
      DetailAST identifier = aAST.findFirstToken(TokenTypes.IDENT);
      return identifier.toString();
   }

   private boolean itsAFieldVariable(DetailAST aAST) {
      return aAST.getParent().getType() == TokenTypes.OBJBLOCK;
   }

   private void reportStyleError(DetailAST aAST, String variableName) {
      log(aAST.getLineNo(), MSG_KEY + variableName);
   }

   private static final class HungarianNotationMemberDetector {

      private Pattern pattern = Pattern.compile("m[A-Z0-9].*");

      public boolean detectsNotation(String variableName) {
         return pattern.matcher(variableName).matches();
      }
   }

}
