package app.repositories;

import app.entities.EntityResultAccesories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository(value = "result_accesories")
public interface ResultAccesories extends CrudRepository<EntityResultAccesories, Long> {

}
