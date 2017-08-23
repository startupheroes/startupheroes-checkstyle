package es.startuphero.checkstyle.util;

/**
 * @author ozlem.ulag
 */
public final class StringUtil {

  public static boolean isEmpty(Object str) {
    return (str == null || "".equals(str));
  }
}
