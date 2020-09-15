package es.startuphero.checkstyle.inputs;

import java.time.LocalDateTime;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import org.hibernate.annotations.ColumnDefault;

import static es.startuphero.checkstyle.inputs.TestEntityForColumnDefaultCheck.StorageCondition.COLD;

@Entity
public class TestEntityForColumnDefaultCheck {

  // Integer type

  @ColumnDefault("0")
  @Column(nullable = false)
  private Integer countWithMatchingDefaults = 0;

  @ColumnDefault(value = "0")
  @Column(nullable = false)
  private Integer countWithMatchingDefaultsAndValueParam = 0;

  @ColumnDefault("0")
  @Column(nullable = false)
  private Integer countWithUnMatchingDefaults = 1;

  @ColumnDefault("0")
  @Column(nullable = false)
  private Integer countWithOnlyColumnDefaultValue;

  @Column(nullable = false)
  private Integer countWithOnlyAssignValue = 0;

  @Column(nullable = false)
  private Integer countWithNoDefault;

  // String type

  @ColumnDefault("'ozlem'")
  @Column(nullable = false)
  private String nameWithMatchingDefaults = "ozlem";

  @ColumnDefault("'cemo'")
  @Column(nullable = false)
  private String nameWithUnMatchingDefaults = "ozlem";

  @ColumnDefault("'ozlem'")
  @Column(nullable = false)
  private String nameWithOnlyColumnDefaultValue;

  @Column(nullable = false)
  private String nameWithOnlyAssignValue = "ozlem";

  @Column(nullable = false)
  private String nameWithNoDefault;

  // Enum type

  @ColumnDefault("'NORMAL'")
  @Column(nullable = false)
  private StorageCondition storageConditionWithMatchingDefaults = StorageCondition.NORMAL;

  @ColumnDefault("'COLD'")
  @Column(nullable = false)
  private StorageCondition storageConditionWithMatchingDefaultsAndStaticImport = COLD;

  @ColumnDefault("'COLD'")
  @Column(nullable = false)
  private StorageCondition storageConditionWithUnMatchingDefaults = StorageCondition.NORMAL;

  @ColumnDefault("'NORMAL'")
  @Column(nullable = false)
  private StorageCondition storageConditionWithUnMatchingDefaultsAndStaticImport = COLD;

  @ColumnDefault("'NORMAL'")
  @Column(nullable = false)
  private StorageCondition storageConditionWithOnlyColumnDefaultValue;

  @Column(nullable = false)
  private StorageCondition storageConditionWithOnlyAssignValue = StorageCondition.NORMAL;

  @Column(nullable = false)
  private StorageCondition storageConditionWithOnlyAssignValueAndStaticImport = COLD;

  @Column(nullable = false)
  private StorageCondition storageConditionWithNoDefault;

  // Long type

  @ColumnDefault("0")
  @Column(nullable = false)
  private Long longCountWithMatchingDefaults = 0L;

  @ColumnDefault("0")
  @Column(nullable = false)
  private Long longCountWithMatchingDefaultsAndSmallType = 0l;

  @ColumnDefault(value = "0")
  @Column(nullable = false)
  private Long longCountWithMatchingDefaultsAndValueParam = 0L;

  @ColumnDefault("0")
  @Column(nullable = false)
  private Long longCountWithUnMatchingDefaults = 1L;

  @ColumnDefault("0")
  @Column(nullable = false)
  private Long longCountWithUnMatchingDefaultsWithSmallType = 1l;

  @ColumnDefault("0")
  @Column(nullable = false)
  private Long longCountWithOnlyColumnDefaultValue;

  @Column(nullable = false)
  private Long longCountWithOnlyAssignValue = 0L;

  @Column(nullable = false)
  private Long longCountWithNoDefault;

  // Double type

  @ColumnDefault("0")
  @Column(nullable = false)
  private Double doubleCountWithMatchingDefaults = 0d;

  @ColumnDefault("0")
  @Column(nullable = false)
  private Double doubleCountWithMatchingDefaultsAndBigType = 0D;

  @ColumnDefault("0.5")
  @Column(nullable = false)
  private Double doubleCountWithMatchingDefaultsAndDecimal = 0.5d;

  @ColumnDefault("0.5")
  @Column(nullable = false)
  private Double doubleCountWithMatchingDefaultsAndBigTypeAndDecimal = 0.5D;

  @ColumnDefault(value = "0.5")
  @Column(nullable = false)
  private Double doubleCountWithMatchingDefaultsAndValueParam = 0.5d;

  @ColumnDefault("0")
  @Column(nullable = false)
  private Double doubleCountWithUnMatchingDefaults = 1d;

  @ColumnDefault("1")
  @Column(nullable = false)
  private Double doubleCountWithUnMatchingDefaultsAndDecimal = 0.5d;

  @ColumnDefault("0")
  @Column(nullable = false)
  private Double doubleCountWithOnlyColumnDefaultValue;

  @Column(nullable = false)
  private Double doubleCountWithOnlyAssignValue = 0d;

  @Column(nullable = false)
  private Double doubleCountWithNoDefault;

  // Character type

  @ColumnDefault("'M'")
  @Column(nullable = false)
  private Character genderWithMatchingDefaults = 'M';

  @ColumnDefault("'M'")
  @Column(nullable = false)
  private Character genderWithUnMatchingDefaults = 'F';

  @ColumnDefault("'M'")
  @Column(nullable = false)
  private Character genderWithOnlyColumnDefaultValue;

  @Column(nullable = false)
  private Character genderWithOnlyAssignValue = 'F';

  @Column(nullable = false)
  private Character genderWithNoDefault;

  // char type

  @ColumnDefault("'M'")
  @Column(nullable = false)
  private char smallGenderWithMatchingDefaults = 'M';

  @ColumnDefault("'M'")
  @Column(nullable = false)
  private char smallGenderWithUnMatchingDefaults = 'F';

  @ColumnDefault("'M'")
  @Column(nullable = false)
  private char smallGenderWithOnlyColumnDefaultValue;

  @Column(nullable = false)
  private char smallGenderWithOnlyAssignValue = 'F';

  @Column(nullable = false)
  private char smallGenderWithNoDefault;

  // Boolean type

  @ColumnDefault("true")
  @Column(nullable = false)
  private Boolean activeWithMatchingDefaults = true;

  @ColumnDefault("false")
  @Column(nullable = false)
  private Boolean activeWithUnMatchingDefaults = true;

  @ColumnDefault("true")
  @Column(nullable = false)
  private Boolean activeWithOnlyColumnDefaultValue;

  @Column(nullable = false)
  private boolean activeWithOnlyAssignValue = false;

  @Column(nullable = false)
  private Boolean activeWithNoDefault;

  // Single quote

  @ColumnDefault("ozlem")
  @Column(nullable = false)
  private String nameWithMatchingDefaultsAndWithoutSingleQuote = "ozlem";

  @ColumnDefault("M")
  @Column(nullable = false)
  private Character genderWithMatchingDefaultsAndWithoutSingleQuote = 'M';

  @ColumnDefault("M")
  @Column(nullable = false)
  private char smallGenderWithMatchingDefaultsAndWithoutSingleQuote = 'M';

  @ColumnDefault("NORMAL")
  @Column(nullable = false)
  private StorageCondition storageConditionWithMatchingDefaultsAndWithoutSingleQuote = StorageCondition.NORMAL;

  @ColumnDefault("COLD")
  @Column(nullable = false)
  private StorageCondition storageConditionWithMatchingDefaultsAndStaticImportAndWithoutSingleQuote = COLD;

  @ColumnDefault("'ozlem")
  @Column(nullable = false)
  private String nameWithMatchingDefaultsAndWithOnlyLeftSingleQuote = "ozlem";

  @ColumnDefault("ozlem'")
  @Column(nullable = false)
  private String nameWithMatchingDefaultsAndWithOnlyRightSingleQuote = "ozlem";

  // Date variable

  @ColumnDefault("CURRENT_TIMESTAMP")
  @Column(nullable = false)
  private Date createdAtWithCurrentTimeStamp;

  @ColumnDefault("CURRENT_TIMESTAMP(6)")
  @Column(nullable = false)
  private Date createdAtWithCurrentTimeStamp6;

  @ColumnDefault("CURRENT_TIMESTAMP(6) on update CURRENT_TIMESTAMP(6)")
  @Column(nullable = false)
  private Date lastUpdatedAtWithCurrentTimeStamp;

  @ColumnDefault("NOW")
  @Column(nullable = false)
  private Date createdAtWithNow;

  @ColumnDefault("NOW(6)")
  @Column(nullable = false)
  private Date createdAtWithNow6;

  @ColumnDefault("LOCALTIME")
  @Column(nullable = false)
  private Date createdAtWithLocalTime;

  @ColumnDefault("LOCALTIME(6)")
  @Column(nullable = false)
  private Date createdAtWithLocalTime6;

  @ColumnDefault("LOCALTIMESTAMP")
  @Column(nullable = false)
  private Date createdAtWithLocalTimeStamp;

  @ColumnDefault("LOCALTIMESTAMP(6)")
  @Column(nullable = false)
  private Date createdAtWithLocalTimeStamp6;

  @Column(nullable = false)
  private Date createdAtWithNoDefault;

  @ColumnDefault("UNKNOWN_TIMESTAMP(6)")
  @Column(nullable = false)
  private Date createdAtWithUnknownTimeStamp;

  @ColumnDefault(value = "CURRENT_TIMESTAMP(6)")
  @Column(nullable = false)
  private Date createdAtWithCurrentTimeStamp6AndValueParam;

  @GeneratedValue
  @ColumnDefault("CURRENT_TIMESTAMP(6)")
  @Column(nullable = false)
  private LocalDateTime createdAtOnLocalDateType;

  @GeneratedValue
  @ColumnDefault("CURRENT_TIMESTAMP(6) on update CURRENT_TIMESTAMP(6)")
  @Column(nullable = false)
  private LocalDateTime lastUpdatedAtOnLocalDateType;

  @Column(nullable = false)
  @ColumnDefault("'1970-01-01 00:00:00'")
  private LocalDateTime localDateTimeWithDefault = LocalDateTime.parse("1970-01-01T00:00:00");

  public enum StorageCondition {

    NORMAL,     // room temperature
    COLD,       // +4 degrees centigrade
    FROZEN      // -18 degrees centigrade
  }
}