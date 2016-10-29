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

}
