package es.startuphero.checkstyle.checks.annotation;

import com.google.common.collect.ImmutableMap;
import es.startuphero.checkstyle.BaseCheckTestSupport;
import java.io.File;
import java.io.IOException;
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
                                             "es.startuphero.checkstyle.inputs.ForbiddenAnnotation")),
           getPath(fileName),
           expectedMessages);
  }

  protected String getPath(String filename) throws IOException {
    return super.getPath("inputs" + File.separator + filename);
  }
}
