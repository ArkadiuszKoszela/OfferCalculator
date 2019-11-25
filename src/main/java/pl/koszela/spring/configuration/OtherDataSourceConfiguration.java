package pl.koszela.spring.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "otherEntityManagerFactory",
        transactionManagerRef = "otherTransactionManager",
        basePackages = {
                "pl.koszela.spring.repositories.other"
        }
)
public class OtherDataSourceConfiguration {

    @Value("${spring.datasource-recommend.url}")
    private String jdbcUrl;
    @Value("${spring.datasource-recommend.username}")
    private String jdbcUsername;
    @Value("${spring.datasource-recommend.password}")
    private String jdbcPassword;

    @Bean(name = "otherDataSource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().url(jdbcUrl).username(jdbcUsername).password(jdbcPassword).build();
    }

    @Bean(name = "otherEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean
    barEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("otherDataSource") DataSource dataSource
    ) {
        return
                builder
                        .dataSource(dataSource)
                        .packages("pl.koszela.spring.entities.other")
                        .persistenceUnit("db2")
                        .build();
    }

    @Bean(name = "otherTransactionManager")
    public PlatformTransactionManager productTransactionManager(
            @Qualifier("otherEntityManagerFactory") EntityManagerFactory productEntityManagerFactory
    ) {
        return new JpaTransactionManager(productEntityManagerFactory);
    }
}
