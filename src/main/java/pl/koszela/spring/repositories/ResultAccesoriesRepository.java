package pl.koszela.spring.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.koszela.spring.entities.EntityResultAccesories;

@Repository(value = "result_accesories")
public interface ResultAccesoriesRepository extends CrudRepository<EntityResultAccesories, Long> {

}
