package es.startupheroes.checkstyle.checks;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;

/**
 * @author ozlem.ulag
 */
public class BlockedAnnotationKeyCheckTest extends BaseCheckTestSupport {

   private static final String MSG_KEY = "blockedAnnotationKeyCheckMessage";

   @Test
   public void testByWrongInput() throws Exception {
      String[] expectedMessages = {"58: " + getCheckMessage(MSG_KEY, "unique", "Column")};
      test("TestWrongEntity.java", expectedMessages);
   }

   @Test
   public void testByCorrectInput() throws Exception {
      String[] expectedMessages = {};
      test("TestCorrectEntity.java", expectedMessages);
   }

   private void test(String fileName, String[] expectedMessages) throws Exception {
      verify(createCheckConfig(BlockedAnnotationKeyCheck.class,
                               ImmutableMap.of("annotationBlockedKeyMap", "javax.persistence.Column:unique")),
             getPath(fileName),
             expectedMessages);
   }

}
