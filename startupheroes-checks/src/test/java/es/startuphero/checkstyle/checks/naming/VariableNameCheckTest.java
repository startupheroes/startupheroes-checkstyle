package es.startuphero.checkstyle.checks.naming;

import com.google.common.collect.ImmutableMap;
import es.startuphero.checkstyle.BaseCheckTestSupport;
import java.io.File;
import java.io.IOException;
import org.junit.Test;

/**
 * @author ozlem.ulag
 */
public class VariableNameCheckTest extends BaseCheckTestSupport {

  private static final String MSG_KEY = "illegal.variable.name";

  @Test
  public void testByWrongInput() throws Exception {
    String[] expectedMessages = {"28: " + getCheckMessage(MSG_KEY, "id"),
                                 "39: " + getCheckMessage(MSG_KEY, "modelId"),
                                 "54: " + getCheckMessage(MSG_KEY, "name"),
                                 "78: " + getCheckMessage(MSG_KEY, "parentId"),
                                 "80: " + getCheckMessage(MSG_KEY, "externalId")};
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

  protected String getPath(String filename) throws IOException {
    return super.getPath("inputs" + File.separator + filename);
  }
}
