package startupheroes.checkstyle.checks;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;

/**
 * @author ozlem.ulag
 */
public class BlockedClassAnnotationCheckTest extends BaseCheckTestSupport {

   private static final String MSG_KEY = "blockedClassAnnotationCheckMessage";

   @Test
   public void testByWrongInput() throws Exception {
      String[] expectedMessages = {"6: " + getCheckMessage(MSG_KEY, "BlockedAnnotation")};
      test("TestRepository.java", expectedMessages);
   }

   @Test
   public void testByCorrectInput() throws Exception {
      String[] expectedMessages = {};
      test("TestInterface.java", expectedMessages);
   }

   private void test(String fileName, String[] expectedMessages) throws Exception {
      verify(createCheckConfig(BlockedClassAnnotationCheck.class,
                               ImmutableMap.of("blockedAnnotations", "startupheroes.checkstyle.checks.BlockedAnnotation")),
             getPath(fileName),
             expectedMessages);
   }

}
