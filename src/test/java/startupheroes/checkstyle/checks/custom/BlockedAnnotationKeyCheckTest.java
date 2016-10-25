package startupheroes.checkstyle.checks.custom;

import com.puppycrawl.tools.checkstyle.DefaultConfiguration;
import org.junit.Test;
import startupheroes.checkstyle.checks.BaseCheckTestSupport;

import static startupheroes.checkstyle.checks.custom.BlockedAnnotationKeyCheck.MSG_KEY;

/**
 * @author ozlem.ulag
 */
public class BlockedAnnotationKeyCheckTest extends BaseCheckTestSupport {

   @Test
   public void testByWrongInput() throws Exception {
      DefaultConfiguration checkConfig = getCheckerConfig();
      String[] expected = {
          "58: " + getCheckMessage(MSG_KEY, "unique", "Column")
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
          createCheckConfig(BlockedAnnotationKeyCheck.class);
      checkConfig.addAttribute("annotationBlockedKeyMap", "Column:unique, javax.persistence.Column:unique");
      return checkConfig;
   }

}
