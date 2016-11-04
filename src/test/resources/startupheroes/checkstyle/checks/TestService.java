package startupheroes.checkstyle.checks;


public class TestService implements TestInterface {

   @Override
   public Boolean redundantPublic() {
      return null;
   }

   @Override
   public Boolean redundantAbstract() {
      return null;
   }

   public void overrideMe() {
      // has not @Override annotation!
   }

   @Override
   public OtherInterface otherOperations() throws RuntimeException {
      try {
         System.out.println("An exception occurred!");
      } catch (NullPointerException expected) {

      } catch (ArrayIndexOutOfBoundsException ignored) {

      } catch (Exception ex) {

      }
      return null;
   }

}
