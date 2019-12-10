package pl.koszela.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.koszela.spring.entities.main.CustomerRecommend;
import pl.koszela.spring.entities.main.UserMobileApp;

@Repository(value = "repo_user_mobile_app")
public interface UserMobileAppRepository extends JpaRepository<UserMobileApp, Long> {
}