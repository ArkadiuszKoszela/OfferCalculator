package app.repositories;

import app.entities.EntityResultTiles;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("result_tiles")
public interface ResultTiles extends CrudRepository<EntityResultTiles, Long> {

    List<EntityResultTiles> findAllByPriceListName(String nazwaCennika);
}
