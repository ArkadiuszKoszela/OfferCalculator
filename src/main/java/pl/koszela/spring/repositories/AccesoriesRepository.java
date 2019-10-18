package pl.koszela.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.koszela.spring.entities.EntityAccesories;

import java.util.List;

@Repository(value = "repo_accesories")
public interface AccesoriesRepository extends JpaRepository<EntityAccesories, Long> {

    List<EntityAccesories> findAllByCategoryEquals (String category);
}