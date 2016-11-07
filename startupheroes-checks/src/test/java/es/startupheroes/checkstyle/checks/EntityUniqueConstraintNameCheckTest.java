package es.startupheroes.checkstyle.checks;

import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

/**
 * @author ozlem.ulag
 */
public class EntityUniqueConstraintNameCheckTest extends BaseCheckTestSupport {

   private static final String MSG_KEY = "entityUniqueConstraintNameCheckMessage";

   private static final String MSG_IDENTIFIER_NAME_TOO_LONG = "databaseIdentifierNameTooLongMessage";

   private static final Integer MAX_LENGTH = 64;

   @Test
   public void testByWrongInput() throws Exception {
      String[] expectedMessages = {"14: " + getCheckMessage(MSG_IDENTIFIER_NAME_TOO_LONG,
                                                            "test_wrong_entity_model_id_attributeValue_1_attributeValue_1_attributeValue_1_uk",
                                                            MAX_LENGTH),
                                   "17: " + getCheckMessage(MSG_KEY, "test_wrong_entity_relative_url_uk")};
      test("TestWrongEntity.java", expectedMessages);
   }

   @Test
   public void testByAbstractEntity() throws Exception {
      String[] expectedMessages = {"15: " + getCheckMessage(MSG_KEY, "abstract_user_list_item_user_id_object_id_uk")};
      test("AbstractUserListItem.java", expectedMessages);
   }

   @Test
   public void testByCorrectInput() throws Exception {
      String[] expectedMessages = {};
      test("TestCorrectEntity.java", expectedMessages);
   }

   private void test(String fileName, String[] expectedMessages) throws Exception {
      Map<String, String> propertyMap = getPropertyMap();
      verify(createCheckConfig(EntityUniqueConstraintNameCheck.class, propertyMap),
             getPath(fileName),
             expectedMessages);
   }

   private Map<String, String> getPropertyMap() {
      Map<String, String> propertyMap = new HashMap<>();
      propertyMap.put("entityAnnotation", "javax.persistence.Entity");
      propertyMap.put("abstractEntityAnnotation", "javax.persistence.MappedSuperclass");
      propertyMap.put("tableAnnotation", "javax.persistence.Table");
      propertyMap.put("uniqueConstraintAnnotation", "javax.persistence.UniqueConstraint");
      propertyMap.put("key", "uniqueConstraints");
      propertyMap.put("keyName", "name");
      propertyMap.put("keyColumns", "columnNames");
      propertyMap.put("suggestedSuffix", "uk");
      propertyMap.put("maxLength", MAX_LENGTH.toString());
      propertyMap.put("regex", "^[a-z0-9_]*$");
      return propertyMap;
   }

}
