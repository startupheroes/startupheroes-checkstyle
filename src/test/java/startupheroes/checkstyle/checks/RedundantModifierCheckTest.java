package startupheroes.checkstyle.checks;

import com.puppycrawl.tools.checkstyle.checks.modifier.RedundantModifierCheck;
import org.junit.Test;

import static com.puppycrawl.tools.checkstyle.checks.modifier.RedundantModifierCheck.MSG_KEY;

/**
 * @author ozlem.ulag
 */
public class RedundantModifierCheckTest extends BaseCheckTestSupport {

   @Test
   public void testByWrongInput() throws Exception {
      String[] expectedMessages = {"5:4: " + getCheckMessage(MSG_KEY, "public"),
                                   "7:4: " + getCheckMessage(MSG_KEY, "static"),
                                   "9:4: " + getCheckMessage(MSG_KEY, "final"),
                                   "11:4: " + getCheckMessage(MSG_KEY, "public"),
                                   "13:4: " + getCheckMessage(MSG_KEY, "abstract")
      };
      verify(createCheckConfig(RedundantModifierCheck.class),
             getPath("TestInterface.java"),
             expectedMessages);
   }

}
