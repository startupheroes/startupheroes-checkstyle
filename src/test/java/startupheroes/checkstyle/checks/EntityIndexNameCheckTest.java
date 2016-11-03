package startupheroes.checkstyle.checks;

import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

/**
 * @author ozlem.ulag
 */
public class EntityIndexNameCheckTest extends BaseCheckTestSupport {

   private static final String MSG_KEY = "entityIndexNameCheckMessage";

   @Test
   public void testByWrongInput() throws Exception {
      String[] expectedMessages = {"20: " + getCheckMessage(MSG_KEY, "test_wrong_entity_sku_index")};
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
      Map<String, String> propertyMap = getPropertyMap();
      verify(createCheckConfig(EntityIndexNameCheck.class, propertyMap),
             getPath(fileName),
             expectedMessages);
   }

   private Map<String, String> getPropertyMap() {
      Map<String, String> propertyMap = new HashMap<>();
      propertyMap.put("entityAnnotation", "javax.persistence.Entity");
      propertyMap.put("abstractEntityAnnotation", "javax.persistence.MappedSuperclass");
      propertyMap.put("tableAnnotation", "javax.persistence.Table");
      propertyMap.put("indexAnnotation", "javax.persistence.Id");
      propertyMap.put("key", "indexes");
      propertyMap.put("keyName", "name");
      propertyMap.put("keyColumns", "columnList");
      propertyMap.put("suggestedSuffix", "index");
      propertyMap.put("maxLength", "64");
      propertyMap.put("regex", "^[a-z0-9_]*$");
      return propertyMap;
   }

}
