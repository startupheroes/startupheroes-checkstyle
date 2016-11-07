package es.startuphero.checkstyle.checks;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;

/**
 * @author ozlem.ulag
 */
public class EntityEqualsHashCodeCheckTest extends BaseCheckTestSupport {

   private static final String MSG_KEY = "entityEqualsHashCodeCheckMessage";

   @Test
   public void testByWrongInput() throws Exception {
      String[] expectedMessages = {"12: " + getCheckMessage(MSG_KEY)};
      test("TestWrongEntity.java", expectedMessages);
   }

   @Test
   public void testByAbstractEntity() throws Exception {
      String[] expectedMessages = {"14: " + getCheckMessage(MSG_KEY)};
      test("AbstractUserListItem.java", expectedMessages);
   }

   @Test
   public void testByCorrectInput() throws Exception {
      String[] expectedMessages = {};
      test("TestCorrectEntity.java", expectedMessages);
   }

   private void test(String fileName, String[] expectedMessages) throws Exception {
      verify(createCheckConfig(EntityEqualsHashCodeCheck.class,
                               ImmutableMap.of("entityAnnotation", "javax.persistence.Entity",
                                               "abstractEntityAnnotation", "javax.persistence.MappedSuperclass",
                                               "idAnnotation", "javax.persistence.Id")),
             getPath(fileName),
             expectedMessages);
   }

}
