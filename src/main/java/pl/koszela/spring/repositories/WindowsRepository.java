package pl.koszela.spring.repositories;

import org.springframework.stereotype.Repository;
import pl.koszela.spring.entities.main.Windows;

@Repository(value = "repo_windows")
public interface WindowsRepository extends BaseRepository<Windows> {
}