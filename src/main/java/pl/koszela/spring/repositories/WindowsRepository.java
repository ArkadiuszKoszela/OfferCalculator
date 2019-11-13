package pl.koszela.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.koszela.spring.entities.Windows;

@Repository(value = "repo_windows")
public interface WindowsRepository /*extends JpaRepository<Windows, Long>*/ extends BaseRepository<Windows> {
}