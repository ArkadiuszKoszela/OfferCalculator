package app.repositories;

import app.entities.EntityKolnierz;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KolnierzRepository extends CrudRepository<EntityKolnierz, Long> {

}
