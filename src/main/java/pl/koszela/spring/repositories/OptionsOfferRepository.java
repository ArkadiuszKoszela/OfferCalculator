package pl.koszela.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.koszela.spring.entities.OptionsOffer;

public interface OptionsOfferRepository extends JpaRepository<OptionsOffer, Long> {
}