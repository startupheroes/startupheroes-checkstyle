package es.startuphero.checkstyle.checks;

import com.google.common.collect.ImmutableMap;
import com.puppycrawl.tools.checkstyle.checks.naming.AbstractClassNameCheck;
import org.junit.Test;

import static com.puppycrawl.tools.checkstyle.checks.naming.AbstractClassNameCheck.MSG_NO_ABSTRACT_CLASS_MODIFIER;

/**
 * @author ozlem.ulag
 */
public class AbstractClassNameCheckTest extends BaseCheckTestSupport {

  @Test
  public void testByWrongInput() throws Exception {
    String[] expectedMessages = {"14:1: " + getCheckMessage(MSG_NO_ABSTRACT_CLASS_MODIFIER,
                                                            "AbstractNotHaveModifier")};
    test("AbstractNotHaveModifier.java", expectedMessages);
  }

  @Test
  public void testByCorrectInput() throws Exception {
    String[] expectedMessages = {};
    test("AbstractUserListItem.java", expectedMessages);
  }

  private void test(String fileName, String[] expectedMessages) throws Exception {
    verify(createCheckConfig(AbstractClassNameCheck.class,
                             ImmutableMap.of("ignoreName", "true")),
           getPath(fileName),
           expectedMessages);
  }
}
