package startupheroes.checkstyle.checks.custom;

import com.puppycrawl.tools.checkstyle.DefaultConfiguration;
import org.junit.Test;
import startupheroes.checkstyle.checks.BaseCheckTestSupport;

import static startupheroes.checkstyle.checks.custom.EntityEqualsHashCodeCheck.MSG_KEY;

/**
 * @author ozlem.ulag
 */
public class EntityEqualsHashCodeCheckTest extends BaseCheckTestSupport {

   @Test
   public void testByWrongInput() throws Exception {
      DefaultConfiguration checkConfig = getCheckerConfig();
      String[] expected = {
          "12: " + getCheckMessage(MSG_KEY)
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
          createCheckConfig(EntityEqualsHashCodeCheck.class);
      checkConfig.addAttribute("entityAnnotations", "Entity, javax.persistence.Entity");
      return checkConfig;
   }

}
