package es.startuphero.checkstyle.checks.annotation;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author huseyinaydin
 */
@RestController
public class TestMissingRequestBody {

  @PostMapping
  public void testPostMapping(Body postMappingRequest) {

  }

  @PutMapping
  public void testPostMapping(Body putMappingRequest) {

  }

  @PatchMapping
  public void testPostMapping(Body patchMappingRequest) {

  }

  @DeleteMapping
  public void testDeleteMapping(Body deleteMappingRequest) {

  }

  public void test(Body body) {

  }

  public void testEmpty() {

  }

  public static class Body {

  }
}
