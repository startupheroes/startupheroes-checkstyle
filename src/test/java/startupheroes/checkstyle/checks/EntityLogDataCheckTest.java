package startupheroes.checkstyle.checks;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;

/**
 * @author ozlem.ulag
 */
public class EntityLogDataCheckTest extends BaseCheckTestSupport {

   private static final String MSG_KEY = "entityLogDataCheckMessage";

   @Test
   public void testInterface() throws Exception {
      String[] expectedMessages = {"31: " + getCheckMessage(MSG_KEY, "request"),
                                   "34: " + getCheckMessage(MSG_KEY, "response"),
                                   "43: " + getCheckMessage(MSG_KEY, "uri")
      };
      verify(createCheckConfig(EntityLogDataCheck.class,
                               ImmutableMap.of("entityAnnotation", "javax.persistence.Entity",
                                               "columnAnnotation", "javax.persistence.Column",
                                               "limitLength", "255")),
             getPath("SmsProviderLog.java"),
             expectedMessages);
   }

}
