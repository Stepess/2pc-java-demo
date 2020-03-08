package ua.stepess.microservices.pcdemo.config;

import lombok.RequiredArgsConstructor;
import org.hibernate.engine.transaction.jta.platform.internal.AtomikosJtaPlatform;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import ua.stepess.microservices.pcdemo.config.properties.MoneyDataSourceProperties;
import ua.stepess.microservices.pcdemo.config.properties.MoneyDataSourceProperties;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Properties;

@Configuration
@RequiredArgsConstructor
@DependsOn("transactionManager")
//@EnableJpaRepositories(basePackageClasses = MoneyBookingRepository.class, entityManagerFactoryRef = "moneyEntityManager")
@EnableConfigurationProperties(MoneyDataSourceProperties.class)
public class MoneyConfig {

    private final JpaVendorAdapter jpaVendorAdapter;
    private final MoneyDataSourceProperties moneyDataSourceProperties;

    @Bean(name = "moneyDataSource", initMethod = "init", destroyMethod = "close")
    public DataSource moneyDataSource() {
        var xaDataSource = new AtomikosDataSourceBean();
        xaDataSource.setXaDataSourceClassName("org.postgresql.xa.PGXADataSource");
        xaDataSource.setUniqueResourceName(moneyDataSourceProperties.getXaResourceName());

        Properties properties = new Properties();
        properties.setProperty("user", moneyDataSourceProperties.getUser());
        properties.setProperty("password", moneyDataSourceProperties.getPassword());
        properties.setProperty("serverName", moneyDataSourceProperties.getServerName());
        properties.setProperty("portNumber", moneyDataSourceProperties.getPort().toString());
        properties.setProperty("databaseName", moneyDataSourceProperties.getDatabase());

        xaDataSource.setXaProperties(properties);

        return xaDataSource;
    }

    @Bean(name = "moneyEntityManager")
    public LocalContainerEntityManagerFactoryBean moneyEntityManager(DataSource moneyDataSource) {
        var properties = new HashMap<String, Object>();
        properties.put("hibernate.transaction.jta.platform", AtomikosJtaPlatform.class.getName());
        properties.put("javax.persistence.transactionType", "JTA");

        var entityManager = new LocalContainerEntityManagerFactoryBean();
        entityManager.setJtaDataSource(moneyDataSource);
        entityManager.setJpaVendorAdapter(jpaVendorAdapter);
        entityManager.setPackagesToScan("ua.stepess.microservices.pcdemo.domain.money");
        entityManager.setPersistenceUnitName("moneyPersistenceUnit");
        entityManager.setJpaPropertyMap(properties);
        return entityManager;
    }
    
}
