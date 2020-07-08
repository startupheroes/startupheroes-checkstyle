package es.startuphero.checkstyle.checks.annotation;

import com.google.common.collect.ImmutableMap;
import es.startuphero.checkstyle.BaseCheckTestSupport;
import java.io.File;
import java.io.IOException;
import org.junit.Test;

/**
 * @author ozlem.ulag
 */
public class ColumnDefaultCheckTest extends BaseCheckTestSupport {

  private static final String NOT_MATCHING_MSG_KEY = "column.default.not.matching";

  private static final String ANNOTATION_DIRECT_EXPRESSION_MSG_KEY =
      "column.default.annotation.direct.expression";

  private static final String ANNOTATION_VALUE_REQUIRE_SINGLE_QUOTE_MSG_KEY =
      "column.default.annotation.value.require.single.quote";

  private static final String REGEX = "^.*?(\\bCURRENT_TIMESTAMP\\b|\\bNOW\\b|\\bLOCALTIME\\b|\\bLOCALTIMESTAMP\\b).*$";

  @Test
  public void test() throws Exception {
    String[] expectedMessages =
        {"19: " + getCheckMessage(ANNOTATION_DIRECT_EXPRESSION_MSG_KEY, "countWithMatchingDefaultsAndValueParam"),
         "23: " + getCheckMessage(NOT_MATCHING_MSG_KEY, "countWithUnMatchingDefaults", "1", "0"),
         "27: " + getCheckMessage(NOT_MATCHING_MSG_KEY, "countWithOnlyColumnDefaultValue", null, "0"),
         "31: " + getCheckMessage(NOT_MATCHING_MSG_KEY, "countWithOnlyAssignValue", "0", null),
         "43: " + getCheckMessage(NOT_MATCHING_MSG_KEY, "nameWithUnMatchingDefaults", "ozlem", "cemo"),
         "47: " + getCheckMessage(NOT_MATCHING_MSG_KEY, "nameWithOnlyColumnDefaultValue", null, "ozlem"),
         "51: " + getCheckMessage(NOT_MATCHING_MSG_KEY, "nameWithOnlyAssignValue", "ozlem", null),
         "67: " + getCheckMessage(NOT_MATCHING_MSG_KEY, "storageConditionWithUnMatchingDefaults", "NORMAL", "COLD"),
         "71: " + getCheckMessage(NOT_MATCHING_MSG_KEY, "storageConditionWithUnMatchingDefaultsAndStaticImport",
                                  "COLD", "NORMAL"),
         "75: " + getCheckMessage(NOT_MATCHING_MSG_KEY, "storageConditionWithOnlyColumnDefaultValue", null, "NORMAL"),
         "79: " + getCheckMessage(NOT_MATCHING_MSG_KEY, "storageConditionWithOnlyAssignValue", "NORMAL", null),
         "82: " + getCheckMessage(NOT_MATCHING_MSG_KEY, "storageConditionWithOnlyAssignValueAndStaticImport",
                                  "COLD", null),
         "98: " + getCheckMessage(ANNOTATION_DIRECT_EXPRESSION_MSG_KEY, "longCountWithMatchingDefaultsAndValueParam"),
         "102: " + getCheckMessage(NOT_MATCHING_MSG_KEY, "longCountWithUnMatchingDefaults", "1", "0"),
         "106: " + getCheckMessage(NOT_MATCHING_MSG_KEY, "longCountWithUnMatchingDefaultsWithSmallType", "1", "0"),
         "110: " + getCheckMessage(NOT_MATCHING_MSG_KEY, "longCountWithOnlyColumnDefaultValue", null, "0"),
         "114: " + getCheckMessage(NOT_MATCHING_MSG_KEY, "longCountWithOnlyAssignValue", "0", null),
         "138: " + getCheckMessage(ANNOTATION_DIRECT_EXPRESSION_MSG_KEY,
                                   "doubleCountWithMatchingDefaultsAndValueParam"),
         "142: " + getCheckMessage(NOT_MATCHING_MSG_KEY, "doubleCountWithUnMatchingDefaults", "1", "0"),
         "146: " + getCheckMessage(NOT_MATCHING_MSG_KEY, "doubleCountWithUnMatchingDefaultsAndDecimal", "0.5", "1"),
         "150: " + getCheckMessage(NOT_MATCHING_MSG_KEY, "doubleCountWithOnlyColumnDefaultValue", null, "0"),
         "154: " + getCheckMessage(NOT_MATCHING_MSG_KEY, "doubleCountWithOnlyAssignValue", "0", null),
         "166: " + getCheckMessage(NOT_MATCHING_MSG_KEY, "genderWithUnMatchingDefaults", "F", "M"),
         "170: " + getCheckMessage(NOT_MATCHING_MSG_KEY, "genderWithOnlyColumnDefaultValue", null, "M"),
         "174: " + getCheckMessage(NOT_MATCHING_MSG_KEY, "genderWithOnlyAssignValue", "F", null),
         "186: " + getCheckMessage(NOT_MATCHING_MSG_KEY, "smallGenderWithUnMatchingDefaults", "F", "M"),
         "190: " + getCheckMessage(NOT_MATCHING_MSG_KEY, "smallGenderWithOnlyColumnDefaultValue", null, "M"),
         "194: " + getCheckMessage(NOT_MATCHING_MSG_KEY, "smallGenderWithOnlyAssignValue", "F", null),
         "206: " + getCheckMessage(NOT_MATCHING_MSG_KEY, "activeWithUnMatchingDefaults", "true", "false"),
         "210: " + getCheckMessage(NOT_MATCHING_MSG_KEY, "activeWithOnlyColumnDefaultValue", null, "true"),
         "214: " + getCheckMessage(NOT_MATCHING_MSG_KEY, "activeWithOnlyAssignValue", "false", null),
         "222: " + getCheckMessage(ANNOTATION_VALUE_REQUIRE_SINGLE_QUOTE_MSG_KEY,
                                   "ColumnDefault", "ozlem",
                                   "nameWithMatchingDefaultsAndWithoutSingleQuote"),
         "226: " + getCheckMessage(ANNOTATION_VALUE_REQUIRE_SINGLE_QUOTE_MSG_KEY,
                                   "ColumnDefault", "M",
                                   "genderWithMatchingDefaultsAndWithoutSingleQuote"),
         "230: " + getCheckMessage(ANNOTATION_VALUE_REQUIRE_SINGLE_QUOTE_MSG_KEY,
                                   "ColumnDefault", "M",
                                   "smallGenderWithMatchingDefaultsAndWithoutSingleQuote"),
         "234: " + getCheckMessage(ANNOTATION_VALUE_REQUIRE_SINGLE_QUOTE_MSG_KEY,
                                   "ColumnDefault", "NORMAL",
                                   "storageConditionWithMatchingDefaultsAndWithoutSingleQuote"),
         "238: " + getCheckMessage(ANNOTATION_VALUE_REQUIRE_SINGLE_QUOTE_MSG_KEY,
                                   "ColumnDefault", "COLD",
                                   "storageConditionWithMatchingDefaultsAndStaticImportAndWithoutSingleQuote"),
         "242: " + getCheckMessage(ANNOTATION_VALUE_REQUIRE_SINGLE_QUOTE_MSG_KEY,
                                   "ColumnDefault", "ozlem",
                                   "nameWithMatchingDefaultsAndWithOnlyLeftSingleQuote"),
         "246: " + getCheckMessage(ANNOTATION_VALUE_REQUIRE_SINGLE_QUOTE_MSG_KEY,
                                   "ColumnDefault", "ozlem",
                                   "nameWithMatchingDefaultsAndWithOnlyRightSingleQuote"),
         "291: " + getCheckMessage(NOT_MATCHING_MSG_KEY, "createdAtWithUnknownTimeStamp", null,
                                   "UNKNOWN_TIMESTAMP(6)"),
         "295: " + getCheckMessage(ANNOTATION_DIRECT_EXPRESSION_MSG_KEY,
                                   "createdAtWithCurrentTimeStamp6AndValueParam")};
    test("TestEntityForColumnDefaultCheck.java", expectedMessages);
  }

  private void test(String fileName, String[] expectedMessages) throws Exception {
    verify(createCheckConfig(ColumnDefaultCheck.class,
                             ImmutableMap.of("typeAnnotation", "javax.persistence.Entity",
                                             "abstractTypeAnnotation", "javax.persistence.MappedSuperclass",
                                             "columnAnnotation", "javax.persistence.Column",
                                             "columnDefaultAnnotation", "org.hibernate.annotations.ColumnDefault",
                                             "excludedColumnDefaultAnnotationValueRegex", REGEX)),
           getPath(fileName),
           expectedMessages);
  }

  protected String getPath(String filename) throws IOException {
    return super.getPath("inputs" + File.separator + filename);
  }
}
