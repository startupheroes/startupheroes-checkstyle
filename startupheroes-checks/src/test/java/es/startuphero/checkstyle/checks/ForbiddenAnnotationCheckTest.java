package es.startuphero.checkstyle.checks;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;

/**
 * @author ozlem.ulag
 */
public class ForbiddenAnnotationCheckTest extends BaseCheckTestSupport {

  private static final String MSG_KEY = "forbidden.annotation";

  @Test
  public void testByWrongInput() throws Exception {
    String[] expectedMessages = {"6: " + getCheckMessage(MSG_KEY, "ForbiddenAnnotation")};
    test("TestRepository.java", expectedMessages);
  }

  @Test
  public void testByCorrectInput() throws Exception {
    String[] expectedMessages = {};
    test("TestInterface.java", expectedMessages);
  }

  private void test(String fileName, String[] expectedMessages) throws Exception {
    verify(createCheckConfig(ForbiddenAnnotationCheck.class,
                             ImmutableMap.of("forbiddenAnnotations",
                                             "es.startuphero.checkstyle.checks.ForbiddenAnnotation")),
           getPath(fileName),
           expectedMessages);
  }
}
