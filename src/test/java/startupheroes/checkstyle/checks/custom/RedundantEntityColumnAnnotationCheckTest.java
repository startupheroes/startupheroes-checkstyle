package startupheroes.checkstyle.checks.custom;

import com.puppycrawl.tools.checkstyle.DefaultConfiguration;
import org.junit.Test;
import startupheroes.checkstyle.checks.BaseCheckTestSupport;

import static startupheroes.checkstyle.checks.custom.RedundantEntityColumnAnnotationCheck.MSG_KEY;

/**
 * @author ozlem.ulag
 */
public class RedundantEntityColumnAnnotationCheckTest extends BaseCheckTestSupport {

   @Test
   public void testEntity() throws Exception {
      DefaultConfiguration checkConfig =
          createCheckConfig(RedundantEntityColumnAnnotationCheck.class);
      String[] expected = {
          "20: " + getCheckMessage(MSG_KEY),
          "28: " + getCheckMessage(MSG_KEY)
      };
      verify(checkConfig, getPath("TestEntity.java"), expected);
   }

}
