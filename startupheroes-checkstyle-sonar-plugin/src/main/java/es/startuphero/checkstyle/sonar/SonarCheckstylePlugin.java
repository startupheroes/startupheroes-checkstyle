package es.startuphero.checkstyle.sonar;

import org.sonar.api.Plugin;

/**
 * @author ozlem.ulag
 */

public final class SonarCheckstylePlugin implements Plugin {

   @Override
   public void define(Context context) {
      context.addExtension(SonarRulesDefinition.class);
   }

}
