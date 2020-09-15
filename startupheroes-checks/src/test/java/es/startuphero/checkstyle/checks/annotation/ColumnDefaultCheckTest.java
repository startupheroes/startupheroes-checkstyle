package es.startuphero.checkstyle.checks.annotation;

import com.google.common.collect.ImmutableMap;
import es.startuphero.checkstyle.BaseCheckTestSupport;
import java.io.File;
import java.io.IOException;
import org.junit.Ignore;
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

  @Ignore
  @Test
  public void test() throws Exception {
    String[] expectedMessages =
        {"21: " + getCheckMessage(ANNOTATION_DIRECT_EXPRESSION_MSG_KEY, "countWithMatchingDefaultsAndValueParam"),
         "25: " + getCheckMessage(NOT_MATCHING_MSG_KEY, "countWithUnMatchingDefaults", "1", "0"),
         "29: " + getCheckMessage(NOT_MATCHING_MSG_KEY, "countWithOnlyColumnDefaultValue", null, "0"),
         "33: " + getCheckMessage(NOT_MATCHING_MSG_KEY, "countWithOnlyAssignValue", "0", null),
         "45: " + getCheckMessage(NOT_MATCHING_MSG_KEY, "nameWithUnMatchingDefaults", "ozlem", "cemo"),
         "49: " + getCheckMessage(NOT_MATCHING_MSG_KEY, "nameWithOnlyColumnDefaultValue", null, "ozlem"),
         "53: " + getCheckMessage(NOT_MATCHING_MSG_KEY, "nameWithOnlyAssignValue", "ozlem", null),
         "69: " + getCheckMessage(NOT_MATCHING_MSG_KEY, "storageConditionWithUnMatchingDefaults", "NORMAL", "COLD"),
         "73: " + getCheckMessage(NOT_MATCHING_MSG_KEY, "storageConditionWithUnMatchingDefaultsAndStaticImport",
                                  "COLD", "NORMAL"),
         "77: " + getCheckMessage(NOT_MATCHING_MSG_KEY, "storageConditionWithOnlyColumnDefaultValue", null, "NORMAL"),
         "81: " + getCheckMessage(NOT_MATCHING_MSG_KEY, "storageConditionWithOnlyAssignValue", "NORMAL", null),
         "84: " + getCheckMessage(NOT_MATCHING_MSG_KEY, "storageConditionWithOnlyAssignValueAndStaticImport",
                                  "COLD", null),
         "100: " + getCheckMessage(ANNOTATION_DIRECT_EXPRESSION_MSG_KEY, "longCountWithMatchingDefaultsAndValueParam"),
         "104: " + getCheckMessage(NOT_MATCHING_MSG_KEY, "longCountWithUnMatchingDefaults", "1", "0"),
         "108: " + getCheckMessage(NOT_MATCHING_MSG_KEY, "longCountWithUnMatchingDefaultsWithSmallType", "1", "0"),
         "112: " + getCheckMessage(NOT_MATCHING_MSG_KEY, "longCountWithOnlyColumnDefaultValue", null, "0"),
         "116: " + getCheckMessage(NOT_MATCHING_MSG_KEY, "longCountWithOnlyAssignValue", "0", null),
         "140: " + getCheckMessage(ANNOTATION_DIRECT_EXPRESSION_MSG_KEY,
                                   "doubleCountWithMatchingDefaultsAndValueParam"),
         "144: " + getCheckMessage(NOT_MATCHING_MSG_KEY, "doubleCountWithUnMatchingDefaults", "1", "0"),
         "148: " + getCheckMessage(NOT_MATCHING_MSG_KEY, "doubleCountWithUnMatchingDefaultsAndDecimal", "0.5", "1"),
         "152: " + getCheckMessage(NOT_MATCHING_MSG_KEY, "doubleCountWithOnlyColumnDefaultValue", null, "0"),
         "156: " + getCheckMessage(NOT_MATCHING_MSG_KEY, "doubleCountWithOnlyAssignValue", "0", null),
         "168: " + getCheckMessage(NOT_MATCHING_MSG_KEY, "genderWithUnMatchingDefaults", "F", "M"),
         "172: " + getCheckMessage(NOT_MATCHING_MSG_KEY, "genderWithOnlyColumnDefaultValue", null, "M"),
         "176: " + getCheckMessage(NOT_MATCHING_MSG_KEY, "genderWithOnlyAssignValue", "F", null),
         "188: " + getCheckMessage(NOT_MATCHING_MSG_KEY, "smallGenderWithUnMatchingDefaults", "F", "M"),
         "192: " + getCheckMessage(NOT_MATCHING_MSG_KEY, "smallGenderWithOnlyColumnDefaultValue", null, "M"),
         "196: " + getCheckMessage(NOT_MATCHING_MSG_KEY, "smallGenderWithOnlyAssignValue", "F", null),
         "208: " + getCheckMessage(NOT_MATCHING_MSG_KEY, "activeWithUnMatchingDefaults", "true", "false"),
         "212: " + getCheckMessage(NOT_MATCHING_MSG_KEY, "activeWithOnlyColumnDefaultValue", null, "true"),
         "216: " + getCheckMessage(NOT_MATCHING_MSG_KEY, "activeWithOnlyAssignValue", "false", null),
         "224: " + getCheckMessage(ANNOTATION_VALUE_REQUIRE_SINGLE_QUOTE_MSG_KEY,
                                   "ColumnDefault", "ozlem",
                                   "nameWithMatchingDefaultsAndWithoutSingleQuote"),
         "228: " + getCheckMessage(ANNOTATION_VALUE_REQUIRE_SINGLE_QUOTE_MSG_KEY,
                                   "ColumnDefault", "M",
                                   "genderWithMatchingDefaultsAndWithoutSingleQuote"),
         "232: " + getCheckMessage(ANNOTATION_VALUE_REQUIRE_SINGLE_QUOTE_MSG_KEY,
                                   "ColumnDefault", "M",
                                   "smallGenderWithMatchingDefaultsAndWithoutSingleQuote"),
         "236: " + getCheckMessage(ANNOTATION_VALUE_REQUIRE_SINGLE_QUOTE_MSG_KEY,
                                   "ColumnDefault", "NORMAL",
                                   "storageConditionWithMatchingDefaultsAndWithoutSingleQuote"),
         "240: " + getCheckMessage(ANNOTATION_VALUE_REQUIRE_SINGLE_QUOTE_MSG_KEY,
                                   "ColumnDefault", "COLD",
                                   "storageConditionWithMatchingDefaultsAndStaticImportAndWithoutSingleQuote"),
         "244: " + getCheckMessage(ANNOTATION_VALUE_REQUIRE_SINGLE_QUOTE_MSG_KEY,
                                   "ColumnDefault", "ozlem",
                                   "nameWithMatchingDefaultsAndWithOnlyLeftSingleQuote"),
         "248: " + getCheckMessage(ANNOTATION_VALUE_REQUIRE_SINGLE_QUOTE_MSG_KEY,
                                   "ColumnDefault", "ozlem",
                                   "nameWithMatchingDefaultsAndWithOnlyRightSingleQuote"),
         "293: " + getCheckMessage(NOT_MATCHING_MSG_KEY, "createdAtWithUnknownTimeStamp", null,
                                   "UNKNOWN_TIMESTAMP(6)"),
         "297: " + getCheckMessage(ANNOTATION_DIRECT_EXPRESSION_MSG_KEY,
                                   "createdAtWithCurrentTimeStamp6AndValueParam"),
         "297: " + getCheckMessage(ANNOTATION_DIRECT_EXPRESSION_MSG_KEY,
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
