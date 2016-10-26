package startupheroes.checkstyle.checks.custom;

import com.puppycrawl.tools.checkstyle.Checker;
import java.io.File;
import java.util.List;
import org.junit.Test;
import startupheroes.checkstyle.checks.BaseCheckTestSupport;

import static org.hamcrest.CoreMatchers.is;

public class AntiHungarianCheckTest extends BaseCheckTestSupport {

   private static final int AMOUNT_OF_HUNGARIAN_MEMBER_ERRORS = 6;

   @Test
   public void testByWrongInput() throws Exception {
      test("TestInputForAntiHungarianCheck.java", AMOUNT_OF_HUNGARIAN_MEMBER_ERRORS);
   }

   @Test
   public void testByCorrectInput() throws Exception {
      test("TestCorrectEntity.java", 0);
   }

   private void test(String fileName, Integer expectedNumberOfErrors) throws Exception {
      Checker antiHungarianChecker = createChecker(createCheckConfig(AntiHungarianCheck.class));
      List<File> files = prepareFilesToBeChecked(fileName);
      int numberOfErrors = antiHungarianChecker.process(files);
      assertThat(numberOfErrors, is(expectedNumberOfErrors));
   }

}
