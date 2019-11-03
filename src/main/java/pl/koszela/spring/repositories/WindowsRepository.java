package pl.koszela.spring.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.koszela.spring.entities.Windows;

@Repository
public interface WindowsRepository extends CrudRepository<Windows, Long> {
}