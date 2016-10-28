package startupheroes.checkstyle.checks;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;

/**
 * @author ozlem.ulag
 */
public class EntityGeneratedPrimaryKeyNameCheckTest extends BaseCheckTestSupport {

   private static final String MSG_KEY = "entityGeneratedPrimaryKeyNameCheckMessage";

   @Test
   public void testByWrongInput() throws Exception {
      String[] expectedMessages = {"25: " + getCheckMessage(MSG_KEY, "id")};
      test("TestWrongEntity.java", expectedMessages);
   }

   @Test
   public void testByCorrectInput() throws Exception {
      String[] expectedMessages = {};
      test("TestCorrectEntity.java", expectedMessages);
   }

   private void test(String fileName, String[] expectedMessages) throws Exception {
      verify(createCheckConfig(EntityGeneratedPrimaryKeyNameCheck.class,
                               ImmutableMap.of("entityAnnotation", "javax.persistence.Entity",
                                               "idAnnotation", "javax.persistence.Id",
                                               "generatedValueAnnotation", "javax.persistence.GeneratedValue",
                                               "suggestedGeneratedPrimaryKeyName", "id")),
             getPath(fileName),
             expectedMessages);
   }

}
