package pl.koszela.spring.configuration;

import org.hibernate.cfg.Environment;
import org.hibernate.tool.schema.spi.SchemaCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.jdbc.DataSourceInitializationMode;
import org.springframework.boot.jdbc.SchemaManagement;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.embedded.DataSourceFactory;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "mainEntityManagerFactory",
        transactionManagerRef = "mainTransactionManager",
        basePackages = {
                "pl.koszela.spring.repositories.main"
        }
)
public class CalculatorDataSourceConfiguration {

    Environment env;

    @Value("${spring.datasource.url}")
    private String jdbcUrl;
    @Value("${spring.datasource.username}")
    private String jdbcUsername;
    @Value("${spring.datasource.password}")
    private String jdbcPassword;

    @Primary
    @Bean(name = "mainDataSource")
    public DataSource customerDataSource() {
        return DataSourceBuilder.create().url(jdbcUrl).username(jdbcUsername).password(jdbcPassword).build();
    }

    @Primary
    @Bean(name = "mainEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean
    entityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("mainDataSource") DataSource dataSource
    ) {
        return builder
                .dataSource(dataSource)
                .packages("pl.koszela.spring.entities.main")
                .persistenceUnit("db1")
                .build();
    }

    @Primary
    @Bean(name = "mainTransactionManager")
    public PlatformTransactionManager customerTransactionManager(
            @Qualifier("mainEntityManagerFactory") EntityManagerFactory customerEntityManagerFactory
    ) {
        return new JpaTransactionManager(customerEntityManagerFactory);
    }
}
