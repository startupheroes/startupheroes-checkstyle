package es.startuphero.checkstyle.checks;

import org.junit.Test;

/**
 * @author ozlem.ulag
 */
public class RedundantDefaultAnnotationParameterAssignCheckTest extends BaseCheckTestSupport {

  private static final String MSG_KEY = "redundant.default.annotation.parameter.assign";

  @Test
  public void testByWrongInput() throws Exception {
    String[] expectedMessages = {
        "66: " + getCheckMessage(MSG_KEY, "nullable"),
        "74: " + getCheckMessage(MSG_KEY, "insertable"),
        "74: " + getCheckMessage(MSG_KEY, "length"),
        "74: " + getCheckMessage(MSG_KEY, "updatable")};
    test("TestWrongEntity.java", expectedMessages);
  }

  @Test
  public void testByCorrectInput() throws Exception {
    String[] expectedMessages = {};
    test("TestCorrectEntity.java", expectedMessages);
  }

  private void test(String fileName, String[] expectedMessages) throws Exception {
    verify(createCheckConfig(RedundantDefaultAnnotationParameterAssignCheck.class),
        getPath(fileName),
        expectedMessages);
  }
}
