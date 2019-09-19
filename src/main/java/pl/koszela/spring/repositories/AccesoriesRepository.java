package pl.koszela.spring.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.koszela.spring.entities.EntityAccesories;

@Repository(value = "repo_accesories")
public interface AccesoriesRepository extends CrudRepository<EntityAccesories, Long> {

}
