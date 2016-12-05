package es.startuphero.checkstyle.checks.whitespace;

import es.startuphero.checkstyle.BaseCheckTestSupport;
import java.io.File;
import java.io.IOException;
import org.junit.Test;

/**
 * @author ozlem.ulag
 */
public class MissingEmptyLineCheckTest extends BaseCheckTestSupport {

  private static final String MSG_KEY = "missing.empty.line";

  @Test
  public void testByWrongEntity() throws Exception {
    String[] expectedMessages = {"189: " + getCheckMessage(MSG_KEY)};
    test("TestWrongEntity.java", expectedMessages);
  }

  @Test
  public void testByWrongInterface() throws Exception {
    String[] expectedMessages = {"23: " + getCheckMessage(MSG_KEY),
                                 "24: " + getCheckMessage(MSG_KEY)};
    test("TestInterface.java", expectedMessages);
  }

  @Test
  public void testByCorrectInput() throws Exception {
    String[] expectedMessages = {};
    test("TestCorrectEntity.java", expectedMessages);
  }

  private void test(String fileName, String[] expectedMessages) throws Exception {
    verify(createCheckConfig(MissingEmptyLineCheck.class),
           getPath(fileName),
           expectedMessages);
  }

  protected String getPath(String filename) throws IOException {
    return super.getPath("inputs" + File.separator + filename);
  }
}
