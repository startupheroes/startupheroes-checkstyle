package startupheroes.checkstyle.checks;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;

/**
 * @author ozlem.ulag
 */
public class EntityEqualsHashCodeCheckTest extends BaseCheckTestSupport {

   private static final String MSG_KEY = "entityEqualsHashCodeCheckMessage";

   @Test
   public void testByWrongInput() throws Exception {
      String[] expectedMessages = {"12: " + getCheckMessage(MSG_KEY)};
      test(expectedMessages, "TestWrongEntity.java");
   }

   @Test
   public void testByCorrectInput() throws Exception {
      String[] expectedMessages = {};
      test(expectedMessages, "TestCorrectEntity.java");
   }

   private void test(String[] expectedMessages, String fileName) throws Exception {
      verify(createCheckConfig(EntityEqualsHashCodeCheck.class,
                               ImmutableMap.of("entityAnnotation", "javax.persistence.Entity")),
             getPath(fileName),
             expectedMessages);
   }

}
