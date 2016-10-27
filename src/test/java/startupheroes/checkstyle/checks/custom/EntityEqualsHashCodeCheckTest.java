package startupheroes.checkstyle.checks.custom;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;
import startupheroes.checkstyle.checks.BaseCheckTestSupport;

/**
 * @author ozlem.ulag
 */
public class EntityEqualsHashCodeCheckTest extends BaseCheckTestSupport {

   @Test
   public void testByWrongInput() throws Exception {
      String[] expectedMessages = {"12: " + getCheckMessage("entityEqualsHashCodeCheckMessage")};
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
