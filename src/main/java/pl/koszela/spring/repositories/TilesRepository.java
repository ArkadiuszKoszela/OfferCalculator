package pl.koszela.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.koszela.spring.entities.Tiles;

import java.util.List;

@Transactional
@Repository(value = "tiles_repo")
public interface TilesRepository extends JpaRepository<Tiles, Long> {

    List<Tiles> findByNameEquals (String name);
    List<Tiles> findByPriceListNameEquals (String priceListName);
}
