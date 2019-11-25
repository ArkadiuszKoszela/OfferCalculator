package pl.koszela.spring.repositories.main;

import org.springframework.stereotype.Repository;
import pl.koszela.spring.entities.main.Windows;

@Repository(value = "repo_windows")
public interface WindowsRepository /*extends JpaRepository<Windows, Long>*/ extends BaseRepository<Windows> {
}