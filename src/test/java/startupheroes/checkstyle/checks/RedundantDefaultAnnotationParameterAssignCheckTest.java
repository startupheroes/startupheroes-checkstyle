package startupheroes.checkstyle.checks;

import org.junit.Test;

/**
 * @author ozlem.ulag
 */
public class RedundantDefaultAnnotationParameterAssignCheckTest extends BaseCheckTestSupport {

   private static final String MSG_KEY = "redundantDefaultAnnotationParameterAssignCheckMessage";

   @Test
   public void testByWrongInput() throws Exception {
      String[] expectedMessages = {
          "63: " + getCheckMessage(MSG_KEY, "nullable"),
          "71: " + getCheckMessage(MSG_KEY, "insertable"),
          "71: " + getCheckMessage(MSG_KEY, "length"),
          "71: " + getCheckMessage(MSG_KEY, "updatable")};
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
