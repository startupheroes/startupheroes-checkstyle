package startupheroes.checkstyle.checks.custom;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;
import startupheroes.checkstyle.checks.BaseCheckTestSupport;

import static startupheroes.checkstyle.checks.custom.EntityMandatoryVariableCheck.MSG_KEY;

/**
 * @author ozlem.ulag
 */
public class EntityMandatoryVariableCheckTest extends BaseCheckTestSupport {

   @Test
   public void testByWrongInput() throws Exception {
      String[] expectedMessages = {"12: " + getCheckMessage(MSG_KEY, "lastUpdatedAt")};
      test("TestWrongEntity.java", expectedMessages);
   }

   @Test
   public void testByCorrectInput() throws Exception {
      String[] expectedMessages = {};
      test("TestCorrectEntity.java", expectedMessages);
   }

   private void test(String fileName, String[] expectedMessages) throws Exception {
      verify(createCheckConfig(EntityMandatoryVariableCheck.class,
                               ImmutableMap.of("entityAnnotations", "Entity, javax.persistence.Entity",
                                               "mandatoryVariables", "createdAt, lastUpdatedAt")),
             getPath(fileName),
             expectedMessages);
   }

}
