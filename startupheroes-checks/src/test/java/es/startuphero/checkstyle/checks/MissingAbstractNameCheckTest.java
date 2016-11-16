package es.startuphero.checkstyle.checks;

import com.google.common.collect.ImmutableMap;
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
}
