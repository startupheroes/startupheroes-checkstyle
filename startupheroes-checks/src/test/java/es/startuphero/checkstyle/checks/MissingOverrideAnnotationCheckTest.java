package es.startuphero.checkstyle.checks;

import org.junit.Ignore;
import org.junit.Test;

/**
 * @author ozlem.ulag
 */
@Ignore
public class MissingOverrideAnnotationCheckTest extends BaseCheckTestSupport {

   private static final String MSG_KEY = "missingOverrideAnnotationCheckMessage";

   @Test
   public void test() throws Exception {
      String[] expectedMessages = {"15: " + getCheckMessage(MSG_KEY)
      };
      verify(createCheckConfig(MissingOverrideAnnotationCheck.class),
             getPath("TestService.java"),
             expectedMessages);
   }

}
