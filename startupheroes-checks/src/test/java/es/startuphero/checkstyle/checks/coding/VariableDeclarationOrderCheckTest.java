package es.startuphero.checkstyle.checks.coding;

import com.google.common.collect.ImmutableMap;
import es.startuphero.checkstyle.BaseCheckTestSupport;
import java.io.File;
import java.io.IOException;
import org.junit.Test;

/**
 * @author ozlem.ulag
 */
public class VariableDeclarationOrderCheckTest extends BaseCheckTestSupport {

  private static final String MSG_KEY = "illegal.variable.declaration.order";

  @Test
  public void testByWrongInput() throws Exception {
    String[] expectedMessages = {"12: " + getCheckMessage(MSG_KEY,
                                                          "LOGGER", "1")};
    test("TestWrongInputForLoggerOrderCheck.java", expectedMessages);
  }

  @Test
  public void testByCorrectInput() throws Exception {
    String[] expectedMessages = {};
    test("TestCorrectInputForLoggerOrderCheck.java", expectedMessages);
  }

  private void test(String fileName, String[] expectedMessages) throws Exception {
    verify(createCheckConfig(VariableDeclarationOrderCheck.class,
                             ImmutableMap.of("variableName", "LOGGER", "declarationOrder", "1")),
           getPath(fileName),
           expectedMessages);
  }

  protected String getPath(String filename) throws IOException {
    return super.getPath("inputs" + File.separator + filename);
  }
}
