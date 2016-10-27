package startupheroes.checkstyle.checks.custom;

import org.junit.Test;
import startupheroes.checkstyle.checks.BaseCheckTestSupport;

/**
 * @author ozlem.ulag
 */
public class LoggerOrderCheckTest extends BaseCheckTestSupport {

   @Test
   public void testByWrongInput() throws Exception {
      String[] expectedMessages = {"12: " + getCheckMessage("loggerOrderCheckMessage")};
      test("TestWrongInputForLoggerOrderCheck.java", expectedMessages);
   }

   @Test
   public void testByCorrectInput() throws Exception {
      String[] expectedMessages = {};
      test("TestCorrectInputForLoggerOrderCheck.java", expectedMessages);
   }

   private void test(String fileName, String[] expectedMessages) throws Exception {
      verify(createCheckConfig(LoggerOrderCheck.class),
             getPath(fileName),
             expectedMessages);
   }

}
