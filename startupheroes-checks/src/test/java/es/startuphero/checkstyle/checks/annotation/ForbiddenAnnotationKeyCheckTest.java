package es.startuphero.checkstyle.checks.annotation;

import com.google.common.collect.ImmutableMap;
import es.startuphero.checkstyle.BaseCheckTestSupport;
import java.io.File;
import java.io.IOException;
import org.junit.Test;

/**
 * @author ozlem.ulag
 */
public class ForbiddenAnnotationKeyCheckTest extends BaseCheckTestSupport {

  private static final String MSG_KEY = "forbidden.annotation.key";

  @Test
  public void testByWrongInput() throws Exception {
    String[] expectedMessages = {"60: " + getCheckMessage(MSG_KEY, "unique", "Column")};
    test("TestWrongEntity.java", expectedMessages);
  }

  @Test
  public void testByCorrectInput() throws Exception {
    String[] expectedMessages = {};
    test("TestCorrectEntity.java", expectedMessages);
  }

  private void test(String fileName, String[] expectedMessages) throws Exception {
    verify(createCheckConfig(ForbiddenAnnotationKeyCheck.class,
                             ImmutableMap.of("annotationForbiddenKeysMap", "javax.persistence.Column:unique")),
           getPath(fileName),
           expectedMessages);
  }

  protected String getPath(String filename) throws IOException {
    return super.getPath("inputs" + File.separator + filename);
  }
}
