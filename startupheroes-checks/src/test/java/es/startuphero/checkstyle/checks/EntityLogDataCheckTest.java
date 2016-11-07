package es.startuphero.checkstyle.checks;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;

/**
 * @author ozlem.ulag
 */
public class EntityLogDataCheckTest extends BaseCheckTestSupport {

   private static final String MSG_KEY = "entityLogDataCheckMessage";

   @Test
   public void testLogEntity() throws Exception {
      String[] expectedMessages = {"31: " + getCheckMessage(MSG_KEY, "request"),
                                   "34: " + getCheckMessage(MSG_KEY, "response"),
                                   "43: " + getCheckMessage(MSG_KEY, "uri")
      };
      test("SmsProviderLog.java", expectedMessages);
   }

   @Test
   public void testByAbstractEntity() throws Exception {
      String[] expectedMessages = {};
      test("AbstractUserListItem.java", expectedMessages);
   }

   private void test(String fileName, String[] expectedMessages) throws Exception {
      verify(createCheckConfig(EntityLogDataCheck.class,
                               ImmutableMap.of("entityAnnotation", "javax.persistence.Entity",
                                               "abstractEntityAnnotation", "javax.persistence.MappedSuperclass",
                                               "columnAnnotation", "javax.persistence.Column",
                                               "limitLength", "255")),
             getPath(fileName),
             expectedMessages);
   }

}
