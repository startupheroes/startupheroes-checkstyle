package es.startuphero.checkstyle.checks.naming;

import es.startuphero.checkstyle.BaseCheckTestSupport;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

/**
 * @author ozlem.ulag
 */
public class TableIdentifierUniqueConstraintNameCheckTest extends BaseCheckTestSupport {

  private static final String MSG_KEY = "illegal.table.identifier.name";

  private static final String MSG_IDENTIFIER_NAME_TOO_LONG = "table.identifier.name.too.long";

  private static final Integer MAX_LENGTH = 64;

  @Test
  public void testByWrongInput() throws Exception {
    String suggestedUcName = "test_wrong_entity_model_id_attributeValue_1_attributeValue_1_attributeValue_1_uk";
    String[] expectedMessages = {"14: " + getCheckMessage(MSG_IDENTIFIER_NAME_TOO_LONG, suggestedUcName, MAX_LENGTH),
                                 "19: " + getCheckMessage(MSG_KEY, "test_wrong_entity_relative_url_uk")};
    test("TestWrongEntity.java", expectedMessages);
  }

  @Test
  public void testByAbstractEntity() throws Exception {
    String[] expectedMessages =
        {"15: " + getCheckMessage(MSG_KEY, "abstract_user_list_item_user_id_object_id_uk")};
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
    propertyMap.put("identifierAnnotation", "javax.persistence.UniqueConstraint");
    propertyMap.put("key", "uniqueConstraints");
    propertyMap.put("keyName", "name");
    propertyMap.put("keyColumns", "columnNames");
    propertyMap.put("suggestedSuffix", "uk");
    propertyMap.put("maxLength", MAX_LENGTH.toString());
    propertyMap.put("regex", "^[a-z0-9_]*$");
    return propertyMap;
  }

  protected String getPath(String filename) throws IOException {
    return super.getPath("inputs" + File.separator + filename);
  }
}
