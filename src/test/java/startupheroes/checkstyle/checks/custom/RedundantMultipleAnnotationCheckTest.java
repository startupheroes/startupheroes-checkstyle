package startupheroes.checkstyle.checks.custom;

import com.puppycrawl.tools.checkstyle.DefaultConfiguration;
import org.junit.Test;
import startupheroes.checkstyle.checks.BaseCheckTestSupport;

import static startupheroes.checkstyle.checks.custom.RedundantMultipleAnnotationCheck.MSG_KEY;

/**
 * @author ozlem.ulag
 */
public class RedundantMultipleAnnotationCheckTest extends BaseCheckTestSupport {

   @Test
   public void testByWrongInput() throws Exception {
      DefaultConfiguration checkConfig = getCheckerConfig();
      String[] expected = {
          "25: " + getCheckMessage(MSG_KEY, "Id", "Column"),
          "33: " + getCheckMessage(MSG_KEY, "javax.persistence.Id", "Column")
      };
      verify(checkConfig, getPath("TestWrongEntity.java"), expected);
   }

   @Test
   public void testByCorrectInput() throws Exception {
      DefaultConfiguration checkConfig = getCheckerConfig();
      String[] expected = {};
      verify(checkConfig, getPath("TestCorrectEntity.java"), expected);
   }

   private static DefaultConfiguration getCheckerConfig() {
      DefaultConfiguration checkConfig =
          createCheckConfig(RedundantMultipleAnnotationCheck.class);
      checkConfig.addAttribute("tokens", "VARIABLE_DEF");
      checkConfig.addAttribute("annotationSet1", "Id, javax.persistence.Id");
      checkConfig.addAttribute("annotationSet2", "Column, javax.persistence.Column");
      return checkConfig;
   }

}
