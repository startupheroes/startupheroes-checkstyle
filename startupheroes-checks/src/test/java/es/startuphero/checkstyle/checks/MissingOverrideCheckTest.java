package es.startuphero.checkstyle.checks;

import org.junit.Ignore;
import org.junit.Test;

/**
 * @author ozlem.ulag
 */
@Ignore
public class MissingOverrideCheckTest extends BaseCheckTestSupport {

  private static final String MSG_KEY = "missing.override";

  @Test
  public void test() throws Exception {
    String[] expectedMessages = {"15: " + getCheckMessage(MSG_KEY)
    };
    verify(createCheckConfig(MissingOverrideCheck.class),
           getPath("TestService.java"),
           expectedMessages);
  }
}
