package startupheroes.checkstyle.checks;

import com.google.common.collect.ImmutableMap;
import com.puppycrawl.tools.checkstyle.checks.blocks.EmptyCatchBlockCheck;
import org.junit.Test;

/**
 * @author ozlem.ulag
 */
public class EmptyCatchBlockTest extends BaseCheckTestSupport {

   @Test
   public void testByCatchBlock() throws Exception {
      String[] expectedMessages = {"28: " + "Empty catch block."};
      test("TestService.java", expectedMessages);
   }

   private void test(String fileName, String[] expectedMessages) throws Exception {
      verify(createCheckConfig(EmptyCatchBlockCheck.class,
                               ImmutableMap.of("exceptionVariableName", "expected|ignored")),
             getPath(fileName),
             expectedMessages);
   }

}
