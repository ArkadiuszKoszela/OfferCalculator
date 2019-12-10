package pl.koszela.spring.repositories;

import org.springframework.stereotype.Repository;
import pl.koszela.spring.entities.main.AccessoriesWindows;

@Repository(value = "repo_accesories_windows")
public interface AccesoriesWindowsRepository extends BaseRepository<AccessoriesWindows> {
}