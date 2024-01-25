package es.startuphero.checkstyle.checks.annotation;

import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author huseyinaydin
 */
@RestController
public class TestCorrectRequestBody {

  @PostMapping
  public void testPostMapping(@RequestBody Body body) {

  }

  @PutMapping
  public void testPutMapping(@RequestBody Body body) {

  }

  @PatchMapping
  public void testPatchMapping(@RequestBody Body body) {

  }

  public void test(Body body) {

  }

  public void testEmpty() {

  }

  public static class Body {

  }
}
