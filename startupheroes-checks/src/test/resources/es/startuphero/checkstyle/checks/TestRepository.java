package es.startuphero.checkstyle.checks;

import java.util.List;
import java.util.Optional;

@BlockedAnnotation // don't use this annotation!
public interface TestRepository {

   //@SqlSelect("SELECT * FROM test_wrong_entity WHERE relative_url = :relativeUrl")
   Optional<TestWrongEntity> getTestWrongEntitiesByRelativeUrl(String relativeUrl);

   //@SqlSelect("SELECT * FROM test_wrong_entity")
   List<TestWrongEntity> getAllTestWrongEntities();

   Integer addTestWrongEntity(TestWrongEntity testWrongEntity);

}
