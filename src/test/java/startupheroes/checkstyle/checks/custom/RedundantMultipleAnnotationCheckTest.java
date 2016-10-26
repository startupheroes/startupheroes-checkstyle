package startupheroes.checkstyle.checks.custom;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;
import startupheroes.checkstyle.checks.BaseCheckTestSupport;

import static startupheroes.checkstyle.checks.custom.RedundantMultipleAnnotationCheck.MSG_KEY;

/**
 * @author ozlem.ulag
 */
public class RedundantMultipleAnnotationCheckTest extends BaseCheckTestSupport {

   @Test
   public void testByWrongInput() throws Exception {
      String[] expectedMessages = {"25: " + getCheckMessage(MSG_KEY, "Id", "Column"),
                                   "33: " + getCheckMessage(MSG_KEY, "javax.persistence.Id", "Column")};
      test("TestWrongEntity.java", expectedMessages);
   }

   @Test
   public void testByCorrectInput() throws Exception {
      String[] expectedMessages = {};
      test("TestCorrectEntity.java", expectedMessages);
   }

   private void test(String fileName, String[] expectedMessages) throws Exception {
      verify(createCheckConfig(RedundantMultipleAnnotationCheck.class,
                               ImmutableMap.of("tokens", "VARIABLE_DEF",
                                               "annotationSet1", "Id, javax.persistence.Id",
                                               "annotationSet2", "Column, javax.persistence.Column")),
             getPath(fileName),
             expectedMessages);
   }

}
