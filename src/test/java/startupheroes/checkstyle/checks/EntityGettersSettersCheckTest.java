package startupheroes.checkstyle.checks;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;

/**
 * @author ozlem.ulag
 */
public class EntityGettersSettersCheckTest extends BaseCheckTestSupport {

   private static final String MSG_KEY = "entityGettersSettersCheckMessage";

   @Test
   public void testByWrongInput() throws Exception {
      String[] expectedMessages = {"51: " + getCheckMessage(MSG_KEY, "productName")};
      test("TestWrongEntity.java", expectedMessages);
   }

   @Test
   public void testByCorrectInput() throws Exception {
      String[] expectedMessages = {};
      test("TestCorrectEntity.java", expectedMessages);
   }

   private void test(String fileName, String[] expectedMessages) throws Exception {
      verify(createCheckConfig(EntityGettersSettersCheck.class,
                               ImmutableMap.of("entityAnnotation", "javax.persistence.Entity")),
             getPath(fileName),
             expectedMessages);
   }

}
