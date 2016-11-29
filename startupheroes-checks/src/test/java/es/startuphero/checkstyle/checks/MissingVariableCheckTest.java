package es.startuphero.checkstyle.checks;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;

/**
 * @author ozlem.ulag
 */
public class MissingVariableCheckTest extends BaseCheckTestSupport {

  private static final String MSG_KEY = "missing.variable";

  @Test
  public void testByWrongInput() throws Exception {
    String[] expectedMessages = {"12: " + getCheckMessage(MSG_KEY, "lastUpdatedAt")};
    test("TestWrongEntity.java", expectedMessages);
  }

  @Test
  public void testByAbstractEntity() throws Exception {
    String[] expectedMessages = {"14: " + getCheckMessage(MSG_KEY, "lastUpdatedAt")};
    test("AbstractUserListItem.java", expectedMessages);
  }

  @Test
  public void testByCorrectInput() throws Exception {
    String[] expectedMessages = {};
    test("TestCorrectEntity.java", expectedMessages);
  }

  @Test
  public void testByEntityExtendsMappedSuperClass() throws Exception {
    String[] expectedMessages = {};
    test("ShoppingListFollow.java", expectedMessages);
  }

  private void test(String fileName, String[] expectedMessages) throws Exception {
    verify(createCheckConfig(MissingVariableCheck.class,
                             ImmutableMap.of("typeAnnotation", "javax.persistence.Entity",
                                             "abstractTypeAnnotation", "javax.persistence.MappedSuperclass",
                                             "mandatoryVariables", "createdAt, lastUpdatedAt")),
           getPath(fileName),
           expectedMessages);
  }
}
