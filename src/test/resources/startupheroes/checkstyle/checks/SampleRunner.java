package startupheroes.checkstyle.checks;

/**
 * @author onurozcan
 */
@BlockedAnnotation
public class SampleRunner {

   public static void main(String[] args) throws Exception {
      System.out.println("This is not a utility class!");
   }

}
