package pl.koszela.spring.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.koszela.spring.entities.EntityWindows;

@Repository
public interface WindowsRepository extends CrudRepository<EntityWindows, Long> {

    EntityWindows findByName (String name);
}