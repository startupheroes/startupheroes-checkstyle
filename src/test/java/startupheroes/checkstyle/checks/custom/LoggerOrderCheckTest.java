package startupheroes.checkstyle.checks.custom;

import com.puppycrawl.tools.checkstyle.Checker;
import com.puppycrawl.tools.checkstyle.DefaultConfiguration;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import startupheroes.checkstyle.checks.BaseCheckTestSupport;

import static org.hamcrest.CoreMatchers.is;
import static startupheroes.checkstyle.checks.custom.LoggerOrderCheck.MSG_KEY;

/**
 * @author ozlem.ulag
 */
public class LoggerOrderCheckTest extends BaseCheckTestSupport {

   @Test
   public void testByWrongInput() throws Exception {
      DefaultConfiguration checkConfig =
          createCheckConfig(LoggerOrderCheck.class);
      String[] expected = {
          "12: " + getCheckMessage(MSG_KEY)
          };
      verify(checkConfig, getPath("TestWrongInputForLoggerOrderCheck.java"), expected);
   }

   @Test
   public void testByCorrectInput() throws Exception {
      Checker checker = createChecker(createCheckConfig(LoggerOrderCheck.class));
      List<File> files = prepareFilesToBeChecked();
      int numberOfErrors = checker.process(files);
      assertThat(numberOfErrors, is(0));
   }

   private List<File> prepareFilesToBeChecked() {
      String testFileName = "TestCorrectInputForLoggerOrderCheck.java";
      URL testFileUrl = getClass().getResource(testFileName);
      File testFile = new File(testFileUrl.getFile());
      List<File> files = new ArrayList<File>();
      files.add(testFile);
      return files;
   }

}
