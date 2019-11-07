package pl.koszela.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.koszela.spring.entities.Fireside;
import pl.koszela.spring.entities.LightningProtectionSystem;

@Repository(value = "repo_lightning_protection_system")
public interface LightningProtectionSystemRepository extends JpaRepository<LightningProtectionSystem, Long> {
}