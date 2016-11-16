package es.startuphero.checkstyle.checks;

import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

/**
 * @author ozlem.ulag
 */
public class IndexNameCheckTest extends BaseCheckTestSupport {

  private static final String MSG_KEY = "illegal.table.identifier.name";

  @Test
  public void testByWrongInput() throws Exception {
    String[] expectedMessages = {"21: " + getCheckMessage(MSG_KEY, "test_wrong_entity_sku_index")};
    test("TestWrongEntity.java", expectedMessages);
  }

  @Test
  public void testByWrongInputNotInCurlyBracket() throws Exception {
    String[] expectedMessages = {"14: " + getCheckMessage(MSG_KEY, "sms_provider_log_sms_id_type_index")};
    test("SmsProviderLog.java", expectedMessages);
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
    verify(createCheckConfig(TableIdentifierNameCheck.class, propertyMap),
        getPath(fileName),
        expectedMessages);
  }

  private Map<String, String> getPropertyMap() {
    Map<String, String> propertyMap = new HashMap<>();
    propertyMap.put("tableAnnotation", "javax.persistence.Table");
    propertyMap.put("identifierAnnotation", "javax.persistence.Id");
    propertyMap.put("key", "indexes");
    propertyMap.put("keyName", "name");
    propertyMap.put("keyColumns", "columnList");
    propertyMap.put("suggestedSuffix", "index");
    propertyMap.put("maxLength", "64");
    propertyMap.put("regex", "^[a-z0-9_]*$");
    return propertyMap;
  }
}
