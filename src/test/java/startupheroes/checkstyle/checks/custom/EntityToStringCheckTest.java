package startupheroes.checkstyle.checks.custom;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;
import startupheroes.checkstyle.checks.BaseCheckTestSupport;

import static startupheroes.checkstyle.checks.custom.EntityToStringCheck.MSG_KEY;

/**
 * @author ozlem.ulag
 */
public class EntityToStringCheckTest extends BaseCheckTestSupport {

   @Test
   public void testByWrongInput() throws Exception {
      String[] expectedMessages = {"12: " + getCheckMessage(MSG_KEY)};
      test("TestWrongEntity.java", expectedMessages);
   }

   @Test
   public void testByCorrectInput() throws Exception {
      String[] expectedMessages = {};
      test("TestCorrectEntity.java", expectedMessages);
   }

   private void test(String fileName, String[] expectedMessages) throws Exception {
      verify(createCheckConfig(EntityToStringCheck.class,
                               ImmutableMap.of("entityAnnotations", "Entity, javax.persistence.Entity")),
             getPath(fileName),
             expectedMessages);
   }

}
