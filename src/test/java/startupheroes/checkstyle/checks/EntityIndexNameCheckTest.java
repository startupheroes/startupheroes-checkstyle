package startupheroes.checkstyle.checks;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;

/**
 * @author ozlem.ulag
 */
public class EntityIndexNameCheckTest extends BaseCheckTestSupport {

   private static final String MSG_KEY = "entityIndexNameCheckMessage";

   @Test
   public void testByWrongInput() throws Exception {
      String[] expectedMessages = {"19: " + getCheckMessage(MSG_KEY, "test_wrong_entity_sku_index")};
      test("TestWrongEntity.java", expectedMessages);
   }

   @Test
   public void testByCorrectInput() throws Exception {
      String[] expectedMessages = {};
      test("TestCorrectEntity.java", expectedMessages);
   }

   private void test(String fileName, String[] expectedMessages) throws Exception {
      verify(createCheckConfig(EntityIndexNameCheck.class,
                               ImmutableMap.of("entityAnnotation", "javax.persistence.Entity",
                                               "tableAnnotation", "javax.persistence.Table",
                                               "indexAnnotation", "javax.persistence.Id",
                                               "suggestedIndexSuffix", "index")),
             getPath(fileName),
             expectedMessages);
   }

}
