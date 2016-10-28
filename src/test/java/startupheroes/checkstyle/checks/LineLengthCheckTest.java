package startupheroes.checkstyle.checks;

import com.puppycrawl.tools.checkstyle.DefaultConfiguration;
import com.puppycrawl.tools.checkstyle.checks.sizes.LineLengthCheck;
import org.junit.Test;

import static com.puppycrawl.tools.checkstyle.checks.sizes.LineLengthCheck.MSG_KEY;

public class LineLengthCheckTest extends BaseCheckTestSupport {

   @Test
   public void testSimple() throws Exception {
      DefaultConfiguration checkConfig =
          createCheckConfig(LineLengthCheck.class);
      checkConfig.addAttribute("max", "80");
      checkConfig.addAttribute("ignorePattern", "^.*is OK.*regexp.*$");
      String[] expected = {
          "18: " + getCheckMessage(MSG_KEY, 80, 81),
          "145: " + getCheckMessage(MSG_KEY, 80, 83),
          };
      verify(checkConfig, getPath("InputSimple.java"), expected);
   }

   @Test
   public void shouldLogActualLineLength() throws Exception {
      DefaultConfiguration checkConfig =
          createCheckConfig(LineLengthCheck.class);
      checkConfig.addAttribute("max", "80");
      checkConfig.addAttribute("ignorePattern", "^.*is OK.*regexp.*$");
      checkConfig.addMessage("maxLineLen", "{0},{1}");
      String[] expected = {
          "18: 80,81",
          "145: 80,83",
          };
      verify(checkConfig, getPath("InputSimple.java"), expected);
   }

}
