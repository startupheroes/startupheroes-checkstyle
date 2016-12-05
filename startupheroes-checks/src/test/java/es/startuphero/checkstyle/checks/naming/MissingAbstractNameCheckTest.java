package es.startuphero.checkstyle.checks.naming;

import com.google.common.collect.ImmutableMap;
import es.startuphero.checkstyle.BaseCheckTestSupport;
import java.io.File;
import java.io.IOException;
import org.junit.Test;

/**
 * @author ozlem.ulag
 */
public class MissingAbstractNameCheckTest extends BaseCheckTestSupport {

  private static final String MSG_KEY = "missing.abstract.name";

  @Test
  public void testByWrongInput() throws Exception {
    String[] expectedMessages = {"14: " + getCheckMessage(MSG_KEY)};
    test("NotHaveAbstractName.java", expectedMessages);
  }

  @Test
  public void testByCorrectInput() throws Exception {
    String[] expectedMessages = {};
    test("AbstractUserListItem.java", expectedMessages);
  }

  private void test(String fileName, String[] expectedMessages) throws Exception {
    verify(createCheckConfig(MissingAbstractNameCheck.class,
                             ImmutableMap.of("abstractTypeAnnotation", "javax.persistence.MappedSuperclass")),
           getPath(fileName),
           expectedMessages);
  }

  protected String getPath(String filename) throws IOException {
    return super.getPath("inputs" + File.separator + filename);
  }
}
