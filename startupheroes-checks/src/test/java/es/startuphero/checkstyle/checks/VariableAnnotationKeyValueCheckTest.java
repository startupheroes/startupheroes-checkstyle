package es.startuphero.checkstyle.checks;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;

/**
 * @author ozlem.ulag
 */
public class VariableAnnotationKeyValueCheckTest extends BaseCheckTestSupport {

  private static final String MSG_KEY = "annotation.key.value";

  @Test
  public void testByWrongInput() throws Exception {
    String[] expectedMessages =
        {"69: " + getCheckMessage(MSG_KEY, "createdAt", "Column", "nullable", "false")};
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
    verify(createCheckConfig(VariableAnnotationKeyValueCheck.class,
        ImmutableMap.of("typeAnnotation", "javax.persistence.Entity",
            "abstractTypeAnnotation", "javax.persistence.MappedSuperclass",
            "variableAnnotationKeyValueTable",
            "createdAt:javax.persistence.Column:nullable:false, lastUpdatedAt:javax.persistence.Column:nullable:false")),
        getPath(fileName),
        expectedMessages);
  }
}
