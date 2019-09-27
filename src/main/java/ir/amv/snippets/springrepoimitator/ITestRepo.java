package ir.amv.snippets.springrepoimitator;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITestRepo extends CrudRepository<TestEntity, Long> {
}
