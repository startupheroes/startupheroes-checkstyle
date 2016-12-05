package es.startuphero.checkstyle.checks.method;

import com.google.common.collect.ImmutableMap;
import es.startuphero.checkstyle.BaseCheckTestSupport;
import java.io.File;
import java.io.IOException;
import org.junit.Test;

/**
 * @author ozlem.ulag
 */
public class MissingToStringCheckTest extends BaseCheckTestSupport {

  private static final String MSG_KEY = "missing.to.string";

  @Test
  public void testByWrongInput() throws Exception {
    String[] expectedMessages = {"12: " + getCheckMessage(MSG_KEY)};
    test("TestWrongEntity.java", expectedMessages);
  }

  @Test
  public void testByAbstractEntity() throws Exception {
    String[] expectedMessages = {};
    test("AbstractUserListItem.java", expectedMessages);
  }

  @Test
  public void testByCorrectInput() throws Exception {
    String[] expectedMessages = {};
    test("TestCorrectEntity.java", expectedMessages);
  }

  private void test(String fileName, String[] expectedMessages) throws Exception {
    verify(createCheckConfig(MissingToStringCheck.class,
                             ImmutableMap.of("typeAnnotation", "javax.persistence.Entity",
                                             "abstractTypeAnnotation", "javax.persistence.MappedSuperclass")),
           getPath(fileName),
           expectedMessages);
  }

  protected String getPath(String filename) throws IOException {
    return super.getPath("inputs" + File.separator + filename);
  }
}
