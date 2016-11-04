package startupheroes.checkstyle.checks;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;

/**
 * @author ozlem.ulag
 */
public class AbstractEntityNameCheckTest extends BaseCheckTestSupport {

   private static final String MSG_KEY = "abstractEntityNameCheckMessage";

   @Test
   public void testByWrongInput() throws Exception {
      String[] expectedMessages = {"14: " + getCheckMessage(MSG_KEY)};
      test("NonAbstractSuperEntity.java", expectedMessages);
   }

   @Test
   public void testByCorrectInput() throws Exception {
      String[] expectedMessages = {};
      test("AbstractUserListItem.java", expectedMessages);
   }

   private void test(String fileName, String[] expectedMessages) throws Exception {
      verify(createCheckConfig(AbstractEntityNameCheck.class,
                               ImmutableMap.of("abstractEntityAnnotation", "javax.persistence.MappedSuperclass")),
             getPath(fileName),
             expectedMessages);
   }
}
