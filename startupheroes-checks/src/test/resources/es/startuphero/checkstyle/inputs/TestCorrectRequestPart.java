package es.startuphero.checkstyle.checks.annotation;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author huseyinaydin
 */
@RestController
public class TestCorrectRequestPart {

  @PostMapping
  public void testPostMapping(@RequestPart Body body) {

  }
}
