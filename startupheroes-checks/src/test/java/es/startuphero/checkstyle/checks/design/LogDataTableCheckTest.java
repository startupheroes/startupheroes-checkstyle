package es.startuphero.checkstyle.checks.design;

import com.google.common.collect.ImmutableMap;
import es.startuphero.checkstyle.BaseCheckTestSupport;
import java.io.File;
import java.io.IOException;
import org.junit.Test;

/**
 * @author ozlem.ulag
 */
public class LogDataTableCheckTest extends BaseCheckTestSupport {

  private static final String MSG_KEY = "keep.log.data.table";

  private static final int COLUMN_LENGTH_LIMIT = 255;

  @Test
  public void testLogEntity() throws Exception {
    String[] expectedMessages = {"31: " + getCheckMessage(MSG_KEY, "request", COLUMN_LENGTH_LIMIT),
                                 "34: " + getCheckMessage(MSG_KEY, "response", COLUMN_LENGTH_LIMIT),
                                 "43: " + getCheckMessage(MSG_KEY, "uri", COLUMN_LENGTH_LIMIT)
    };
    test("SmsProviderLog.java", expectedMessages);
  }

  @Test
  public void testByAbstractEntity() throws Exception {
    String[] expectedMessages = {};
    test("AbstractUserListItem.java", expectedMessages);
  }

  private void test(String fileName, String[] expectedMessages) throws Exception {
    verify(createCheckConfig(LogDataTableCheck.class,
                             ImmutableMap.of("typeAnnotation", "javax.persistence.Entity",
                                             "abstractTypeAnnotation", "javax.persistence.MappedSuperclass",
                                             "columnAnnotation", "javax.persistence.Column",
                                             "limitLength", "255")),
           getPath(fileName),
           expectedMessages);
  }

  protected String getPath(String filename) throws IOException {
    return super.getPath("inputs" + File.separator + filename);
  }
}
