package es.startuphero.checkstyle.checks.annotation;

import es.startuphero.checkstyle.BaseCheckTestSupport;
import java.io.File;
import java.io.IOException;
import org.junit.Test;

/**
 * @author ozlem.ulag
 */
public class RedundantDefaultAnnotationParameterAssignCheckTest extends BaseCheckTestSupport {

  private static final String MSG_KEY = "redundant.default.annotation.parameter.assign";

  @Test
  public void testByWrongInput() throws Exception {
    String[] expectedMessages = {
        "67: " + getCheckMessage(MSG_KEY, "nullable"),
        "75: " + getCheckMessage(MSG_KEY, "insertable"),
        "75: " + getCheckMessage(MSG_KEY, "length"),
        "75: " + getCheckMessage(MSG_KEY, "updatable")};
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

  protected String getPath(String filename) throws IOException {
    return super.getPath("inputs" + File.separator + filename);
  }
}
