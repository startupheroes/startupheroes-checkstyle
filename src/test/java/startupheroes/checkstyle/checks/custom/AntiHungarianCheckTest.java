package startupheroes.checkstyle.checks.custom;

import com.puppycrawl.tools.checkstyle.Checker;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import startupheroes.checkstyle.checks.BaseCheckTestSupport;

import static org.hamcrest.CoreMatchers.is;

public class AntiHungarianCheckTest extends BaseCheckTestSupport {

   private static final int AMOUNT_OF_HUNGARIAN_MEMBER_ERRORS = 6;

   @Test
   public void testHungarianMembersByIgnoringLocalVariables() throws Exception {
      Checker checker = createChecker(createCheckConfig(AntiHungarianCheck.class));
      List<File> files = prepareFilesToBeChecked();
      int numberOfErrors = checker.process(files);
      assertThat(numberOfErrors, is(AMOUNT_OF_HUNGARIAN_MEMBER_ERRORS));
   }

   private List<File> prepareFilesToBeChecked() {
      String testFileName = "TestInputForAntiHungarianCheck.java";
      URL testFileUrl = getClass().getResource(testFileName);
      File testFile = new File(testFileUrl.getFile());
      List<File> files = new ArrayList<File>();
      files.add(testFile);
      return files;
   }

}
