package es.startuphero.checkstyle.checks;

import com.google.common.collect.ImmutableMap;
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
}
