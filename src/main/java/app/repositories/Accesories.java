package app.repositories;

import app.entities.EntityAccesories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository(value = "repo_accesories")
public interface Accesories extends CrudRepository<EntityAccesories, Long> {

}
