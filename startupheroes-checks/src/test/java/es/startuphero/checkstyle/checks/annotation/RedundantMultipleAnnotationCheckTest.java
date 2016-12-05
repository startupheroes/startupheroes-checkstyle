package es.startuphero.checkstyle.checks.annotation;

import com.google.common.collect.ImmutableMap;
import es.startuphero.checkstyle.BaseCheckTestSupport;
import java.io.File;
import java.io.IOException;
import org.junit.Test;

/**
 * @author ozlem.ulag
 */
public class RedundantMultipleAnnotationCheckTest extends BaseCheckTestSupport {

  private static final String MSG_KEY = "redundant.multiple.annotation";

  @Test
  public void testByWrongInput() throws Exception {
    String[] expectedMessages = {"28: " + getCheckMessage(MSG_KEY, "Id", "Column")};
    test("TestWrongEntity.java", expectedMessages);
  }

  @Test
  public void testByCorrectInput() throws Exception {
    String[] expectedMessages = {};
    test("TestCorrectEntity.java", expectedMessages);
  }

  private void test(String fileName, String[] expectedMessages) throws Exception {
    verify(createCheckConfig(RedundantMultipleAnnotationCheck.class,
                             ImmutableMap.of("tokens", "VARIABLE_DEF",
                                             "redundantAnnotationPairs",
                                             "javax.persistence.Id:javax.persistence.Column")),
           getPath(fileName),
           expectedMessages);
  }

  protected String getPath(String filename) throws IOException {
    return super.getPath("inputs" + File.separator + filename);
  }
}
