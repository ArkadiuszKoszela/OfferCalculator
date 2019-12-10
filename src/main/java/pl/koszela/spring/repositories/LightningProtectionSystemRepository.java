package pl.koszela.spring.repositories;

import org.springframework.stereotype.Repository;
import pl.koszela.spring.entities.main.LightningProtectionSystem;

@Repository(value = "repo_lightning_protection_system")
public interface LightningProtectionSystemRepository extends BaseRepository<LightningProtectionSystem>{
}