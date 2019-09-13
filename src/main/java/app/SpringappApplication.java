package app;

import com.vaadin.flow.spring.annotation.EnableVaadin;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@ServletComponentScan
@EnableJpaAuditing
@SpringBootApplication
@EnableVaadin
public class SpringappApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringappApplication.class, args);
    }

}
