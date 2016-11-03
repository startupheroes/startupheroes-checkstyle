package startupheroes.checkstyle.checks;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;

/**
 * @author ozlem.ulag
 */
public class CustomHideUtilityClassConstructorCheckTest extends BaseCheckTestSupport {

   @Test
   public void testByCorrectInput() throws Exception {
      String[] expectedMessages = {};
      test("SampleRunner.java", expectedMessages);
   }

   private void test(String fileName, String[] expectedMessages) throws Exception {
      verify(createCheckConfig(CustomHideUtilityClassConstructorCheck.class,
                               ImmutableMap.of("exceptionalAnnotations", "org.springframework.boot.autoconfigure.SpringBootApplication, " +
                                                                         "org.springframework.context.annotation.Configuration, " +
                                                                         "startupheroes.checkstyle.checks.BlockedAnnotation")),
             getPath(fileName),
             expectedMessages);
   }

}
