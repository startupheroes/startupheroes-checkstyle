package es.startuphero.checkstyle.checks;

import com.puppycrawl.tools.checkstyle.checks.modifier.RedundantModifierCheck;
import org.junit.Test;

import static com.puppycrawl.tools.checkstyle.checks.modifier.RedundantModifierCheck.MSG_KEY;

/**
 * @author ozlem.ulag
 */
public class RedundantModifierCheckTest extends BaseCheckTestSupport {

  @Test
  public void testInterface() throws Exception {
    String[] expectedMessages = {"5:3: " + getCheckMessage(MSG_KEY, "public"),
        "7:3: " + getCheckMessage(MSG_KEY, "static"),
        "9:3: " + getCheckMessage(MSG_KEY, "final"),
        "11:3: " + getCheckMessage(MSG_KEY, "public"),
        "13:3: " + getCheckMessage(MSG_KEY, "abstract")
    };
    verify(createCheckConfig(RedundantModifierCheck.class),
        getPath("TestInterface.java"),
        expectedMessages);
  }
}
