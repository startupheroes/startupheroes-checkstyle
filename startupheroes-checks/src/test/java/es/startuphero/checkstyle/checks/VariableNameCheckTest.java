package es.startuphero.checkstyle.checks;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;

/**
 * @author ozlem.ulag
 */
public class VariableNameCheckTest extends BaseCheckTestSupport {

  private static final String MSG_KEY = "illegal.variable.name";

  @Test
  public void testByWrongInput() throws Exception {
    String[] expectedMessages = {"27: " + getCheckMessage(MSG_KEY, "id"),
        "38: " + getCheckMessage(MSG_KEY, "modelId"),
        "53: " + getCheckMessage(MSG_KEY, "name"),
        "77: " + getCheckMessage(MSG_KEY, "parentId"),
        "79: " + getCheckMessage(MSG_KEY, "externalId")};
    test("TestWrongEntity.java", expectedMessages);
  }

  @Test
  public void testByAbstractEntity() throws Exception {
    String[] expectedMessages = {"18: " + getCheckMessage(MSG_KEY, "id")};
    test("AbstractUserListItem.java", expectedMessages);
  }

  @Test
  public void testByCorrectInput() throws Exception {
    String[] expectedMessages = {};
    test("TestCorrectEntity.java", expectedMessages);
  }

  private void test(String fileName, String[] expectedMessages) throws Exception {
    verify(createCheckConfig(VariableNameCheck.class,
        ImmutableMap.of("typeAnnotation", "javax.persistence.Entity",
            "abstractTypeAnnotation", "javax.persistence.MappedSuperclass")),
        getPath(fileName),
        expectedMessages);
  }
}
