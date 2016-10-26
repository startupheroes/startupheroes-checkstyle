package startupheroes.checkstyle.checks.custom;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;
import startupheroes.checkstyle.checks.BaseCheckTestSupport;

import static startupheroes.checkstyle.checks.custom.EntityGettersSettersCheck.MSG_KEY;

/**
 * @author ozlem.ulag
 */
public class EntityGettersSettersCheckTest extends BaseCheckTestSupport {

   @Test
   public void testByWrongInput() throws Exception {
      String[] expectedMessages = {"52: " + getCheckMessage(MSG_KEY, "productName")};
      test("TestWrongEntity.java", expectedMessages);
   }

   @Test
   public void testByCorrectInput() throws Exception {
      String[] expectedMessages = {};
      test("TestCorrectEntity.java", expectedMessages);
   }

   private void test(String fileName, String[] expectedMessages) throws Exception {
      verify(createCheckConfig(EntityGettersSettersCheck.class,
                               ImmutableMap.of("entityAnnotations", "Entity, javax.persistence.Entity")),
             getPath(fileName),
             expectedMessages);
   }

}
