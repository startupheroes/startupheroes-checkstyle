package startupheroes.checkstyle.checks;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;

/**
 * @author ozlem.ulag
 */
public class EntityVariableNameCheckTest extends BaseCheckTestSupport {

   private static final String MSG_KEY = "entityVariableNameCheckMessage";

   @Test
   public void testByWrongInput() throws Exception {
      String[] expectedMessages = {"26: " + getCheckMessage(MSG_KEY, "id"),
                                   "37: " + getCheckMessage(MSG_KEY, "modelId"),
                                   "52: " + getCheckMessage(MSG_KEY, "name"),
                                   "75: " + getCheckMessage(MSG_KEY, "parentId"),
                                   "77: " + getCheckMessage(MSG_KEY, "externalId")};
      test("TestWrongEntity.java", expectedMessages);
   }

   @Test
   public void testByCorrectInput() throws Exception {
      String[] expectedMessages = {};
      test("TestCorrectEntity.java", expectedMessages);
   }

   private void test(String fileName, String[] expectedMessages) throws Exception {
      verify(createCheckConfig(EntityVariableNameCheck.class,
                               ImmutableMap.of("entityAnnotation", "javax.persistence.Entity")),
             getPath(fileName),
             expectedMessages);
   }

}
