package startupheroes.checkstyle.checks;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;

/**
 * @author ozlem.ulag
 */
public class EntityVariableAnnotationKeyValueCheckTest extends BaseCheckTestSupport {

   private static final String MSG_KEY = "entityAnnotationKeyValueCheckMessage";

   @Test
   public void testByWrongInput() throws Exception {
      String[] expectedMessages = {"67: " + getCheckMessage(MSG_KEY, "createdAt", "Column", "nullable", "false")};
      test("TestWrongEntity.java", expectedMessages);
   }

   @Test
   public void testByCorrectInput() throws Exception {
      String[] expectedMessages = {};
      test("TestCorrectEntity.java", expectedMessages);
   }

   private void test(String fileName, String[] expectedMessages) throws Exception {
      verify(createCheckConfig(EntityVariableAnnotationKeyValueCheck.class,
                               ImmutableMap.of("entityAnnotation", "javax.persistence.Entity",
                                               "variableAnnotationKeyValueTable",
                                               "createdAt:javax.persistence.Column:nullable:false, lastUpdatedAt:javax.persistence.Column:nullable:false")),
             getPath(fileName),
             expectedMessages);
   }

}
