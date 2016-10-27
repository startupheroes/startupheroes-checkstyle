package startupheroes.checkstyle.checks.custom;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;
import startupheroes.checkstyle.checks.BaseCheckTestSupport;

/**
 * @author ozlem.ulag
 */
public class BlockedAnnotationKeyCheckTest extends BaseCheckTestSupport {

   @Test
   public void testByWrongInput() throws Exception {
      String[] expectedMessages = {"58: " + getCheckMessage("blockedAnnotationKeyCheckMessage", "unique", "Column")};
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
