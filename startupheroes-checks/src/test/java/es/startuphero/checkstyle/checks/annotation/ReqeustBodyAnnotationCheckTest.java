package es.startuphero.checkstyle.checks.annotation;

import es.startuphero.checkstyle.BaseCheckTestSupport;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import org.junit.Test;

/**
 * @author huseyinaydin
 */
public class ReqeustBodyAnnotationCheckTest extends BaseCheckTestSupport {

  private static final String MSG_KEY = "request.body.annotation.check";

  @Test
  public void testMissingRequestBody() throws Exception {
    String[] expectedMessages = {"16: " + getCheckMessage(MSG_KEY, "postMappingRequest"),
                                 "21: " + getCheckMessage(MSG_KEY, "putMappingRequest"),
                                 "26: " + getCheckMessage(MSG_KEY, "patchMappingRequest")};
    test("TestMissingRequestBody.java", expectedMessages);
  }

  @Test
  public void testCorrectRequestBody() throws Exception {
    String[] expectedMessages = {};
    test("TestCorrectRequestBody.java", expectedMessages);
  }

  @Test
  public void testCorrectRequestPart() throws Exception {
    String[] expectedMessages = {};
    test("TestCorrectRequestPart.java", expectedMessages);
  }

  private void test(String fileName, String[] expectedMessages) throws Exception {
    verify(createCheckConfig(RequestBodyAnnotationCheck.class,
                             Map.of("postMapping", "org.springframework.web.bind.annotation.PostMapping",
                                    "putMapping", "org.springframework.web.bind.annotation.PutMapping",
                                    "patchMapping", "org.springframework.web.bind.annotation.PatchMapping",
                                    "requestBody", "org.springframework.web.bind.annotation.RequestBody")),
           getPath(fileName),
           expectedMessages);
  }

  protected String getPath(String filename) throws IOException {
    return super.getPath("inputs" + File.separator + filename);
  }
}
