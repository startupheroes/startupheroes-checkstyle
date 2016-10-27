package startupheroes.checkstyle.checks.custom;

import org.junit.Test;
import startupheroes.checkstyle.checks.BaseCheckTestSupport;

/**
 * @author ozlem.ulag
 */
public class RedundantDefaultAnnotationParameterAssignCheckTest extends BaseCheckTestSupport {

   private static final String MSG_KEY = "redundantDefaultAnnotationParameterAssignCheckMessage";

   @Test
   public void testByWrongInput() throws Exception {
      String[] expectedMessages = {
          "64: " + getCheckMessage(MSG_KEY, "nullable"),
          "72: " + getCheckMessage(MSG_KEY, "insertable"),
          "72: " + getCheckMessage(MSG_KEY, "length"),
          "72: " + getCheckMessage(MSG_KEY, "updatable")};
      test("TestWrongEntity.java", expectedMessages);
   }

   @Test
   public void testByCorrectInput() throws Exception {
      String[] expectedMessages = {};
      test("TestCorrectEntity.java", expectedMessages);
   }

   private void test(String fileName, String[] expectedMessages) throws Exception {
      verify(createCheckConfig(RedundantDefaultAnnotationParameterAssignCheck.class),
             getPath(fileName),
             expectedMessages);
   }

}
