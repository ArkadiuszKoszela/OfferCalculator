package pl.koszela.spring.repositories.other;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.koszela.spring.entities.main.Windows;
import pl.koszela.spring.entities.other.CustomerRecommend;
import pl.koszela.spring.repositories.main.BaseRepository;

@Repository(value = "repo_recommend_customer")
public interface CustomerRecommendRepository extends JpaRepository<CustomerRecommend, Long> {
}