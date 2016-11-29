package es.startuphero.checkstyle.checks;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;

/**
 * @author ozlem.ulag
 */
public class GeneratedPrimaryKeyNameCheckTest extends BaseCheckTestSupport {

  private static final String MSG_KEY = "generated.primary.key.name";

  @Test
  public void testByWrongInput() throws Exception {
    String[] expectedMessages = {"27: " + getCheckMessage(MSG_KEY, "id")};
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
    verify(createCheckConfig(GeneratedPrimaryKeyNameCheck.class,
                             ImmutableMap.of("typeAnnotation", "javax.persistence.Entity",
                                             "abstractTypeAnnotation", "javax.persistence.MappedSuperclass",
                                             "idAnnotation", "javax.persistence.Id",
                                             "generatedValueAnnotation", "javax.persistence.GeneratedValue",
                                             "suggestedGeneratedPrimaryKeyName", "id")),
           getPath(fileName),
           expectedMessages);
  }
}
