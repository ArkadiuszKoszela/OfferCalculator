package app.repositories;

import app.entities.EntityWindows;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WindowsRepository extends CrudRepository<EntityWindows, Long> {

}
