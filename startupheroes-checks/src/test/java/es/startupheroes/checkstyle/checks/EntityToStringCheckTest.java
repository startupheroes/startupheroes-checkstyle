package es.startupheroes.checkstyle.checks;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;

/**
 * @author ozlem.ulag
 */
public class EntityToStringCheckTest extends BaseCheckTestSupport {

   private static final String MSG_KEY = "entityToStringCheckMessage";

   @Test
   public void testByWrongInput() throws Exception {
      String[] expectedMessages = {"12: " + getCheckMessage(MSG_KEY)};
      test("TestWrongEntity.java", expectedMessages);
   }

   @Test
   public void testByAbstractEntity() throws Exception {
      String[] expectedMessages = {};
      test("AbstractUserListItem.java", expectedMessages);
   }

   @Test
   public void testByCorrectInput() throws Exception {
      String[] expectedMessages = {};
      test("TestCorrectEntity.java", expectedMessages);
   }

   private void test(String fileName, String[] expectedMessages) throws Exception {
      verify(createCheckConfig(EntityToStringCheck.class,
                               ImmutableMap.of("entityAnnotation", "javax.persistence.Entity",
                                               "abstractEntityAnnotation", "javax.persistence.MappedSuperclass")),
             getPath(fileName),
             expectedMessages);
   }

}
