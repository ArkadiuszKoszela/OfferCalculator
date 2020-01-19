package pl.koszela.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.koszela.spring.entities.main.CustomerRecommend;

@Repository(value = "recommend_customer")
public interface CustomerRecommendRepository extends JpaRepository<CustomerRecommend, Long> {
}