package es.startuphero.checkstyle.checks.annotation;

import com.google.common.collect.ImmutableMap;
import es.startuphero.checkstyle.BaseCheckTestSupport;
import java.io.File;
import java.io.IOException;
import org.junit.Test;

/**
 * @author ozlem.ulag
 */
public class VariableAnnotationKeyValueCheckTest extends BaseCheckTestSupport {

  private static final String MSG_KEY = "annotation.key.value";

  @Test
  public void testByWrongInput() throws Exception {
    String[] expectedMessages =
        {"70: " + getCheckMessage(MSG_KEY, "createdAt", "Column", "nullable", "false")};
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
                                             "createdAt:javax.persistence.Column:nullable:false, "
                                             + "lastUpdatedAt:javax.persistence.Column:nullable:false")),
           getPath(fileName),
           expectedMessages);
  }

  protected String getPath(String filename) throws IOException {
    return super.getPath("inputs" + File.separator + filename);
  }
}
