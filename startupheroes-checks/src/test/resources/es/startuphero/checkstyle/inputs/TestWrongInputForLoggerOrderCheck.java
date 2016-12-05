package es.startuphero.checkstyle.inputs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestWrongInputForLoggerOrderCheck {

  private static final String FIELD_1 = "Field_1";

  private static final String FIELD_2 = "Field_2";

  private static final Logger LOGGER =
      LoggerFactory.getLogger(TestWrongInputForLoggerOrderCheck.class);
}
