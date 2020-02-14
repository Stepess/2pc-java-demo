package ua.stepess.microservices.pcdemo.config;

import org.hibernate.engine.transaction.jta.platform.internal.AtomikosJtaPlatform;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import ua.stepess.microservices.pcdemo.config.properties.FlyDataSourceProperties;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Properties;

@Configuration
@DependsOn("transactionManager")
@EnableJpaRepositories(basePackages = "ua.stepess.microservices.pcdemo.persistence.fly",
        entityManagerFactoryRef = "flyEntityManager", transactionManagerRef = "transactionManager")
@EnableConfigurationProperties(FlyDataSourceProperties.class)
public class FlyBookingConfig {

    @Autowired
    private JpaVendorAdapter jpaVendorAdapter;

    @Autowired
    private FlyDataSourceProperties flyDataSourceProperties;

    @Bean(name = "flyDataSource", initMethod = "init", destroyMethod = "close")
    public DataSource customerDataSource() {
        AtomikosDataSourceBean xaDataSource = new AtomikosDataSourceBean();
        xaDataSource.setXaDataSourceClassName("org.postgresql.xa.PGXADataSource");
        xaDataSource.setUniqueResourceName("fly-postgres-db");

        Properties properties = new Properties();
        properties.setProperty ( "user" , "postgres" );
        properties.setProperty ( "password" , "postgres123" );
        properties.setProperty ( "serverName" , "localhost" );
        properties.setProperty ( "portNumber" , "5436" );
        properties.setProperty ( "databaseName" , "postgres" );

        xaDataSource.setXaProperties(properties);

        return xaDataSource;
    }

    @Bean(name = "flyEntityManager")
    @DependsOn("transactionManager")
    public LocalContainerEntityManagerFactoryBean flyEntityManager() throws Throwable {
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.transaction.jta.platform", AtomikosJtaPlatform.class.getName());
        properties.put("javax.persistence.transactionType", "JTA");

        LocalContainerEntityManagerFactoryBean entityManager = new LocalContainerEntityManagerFactoryBean();
        entityManager.setJtaDataSource(customerDataSource());
        entityManager.setJpaVendorAdapter(jpaVendorAdapter);
        entityManager.setPackagesToScan("ua.stepess.microservices.pcdemo.domain.fly");
        entityManager.setPersistenceUnitName("flyPersistenceUnit");
        entityManager.setJpaPropertyMap(properties);
        return entityManager;
    }

}
