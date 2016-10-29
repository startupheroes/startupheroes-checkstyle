package startupheroes.checkstyle.checks;

import com.clovify.lyso.core.annotation.LysoRepository;

@LysoRepository
public interface TestRepository {

   public static final String REDUNDANT_FIELD_PUBLIC = "Redundant public used!";

   static final String REDUNDANT_FIELD_STATIC = "Redundant static used!";

   final String REDUNDANT_FIELD_FINAL = "Redundant final used!";

   public Boolean redundantPublic();

   abstract Boolean redundantAbstract();

   void overrideMe();

}
