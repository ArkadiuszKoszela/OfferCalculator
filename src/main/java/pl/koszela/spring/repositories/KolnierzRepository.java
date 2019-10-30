package pl.koszela.spring.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.koszela.spring.entities.EntityKolnierz;

@Repository
public interface KolnierzRepository extends CrudRepository<EntityKolnierz, Long> {
}