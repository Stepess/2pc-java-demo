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
import ua.stepess.microservices.pcdemo.config.properties.HotelDataSourceProperties;
import ua.stepess.microservices.pcdemo.persistence.hotel.HotelBookingRepository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Properties;

@Configuration
@RequiredArgsConstructor
@DependsOn("transactionManager")
@EnableJpaRepositories(basePackageClasses = HotelBookingRepository.class, entityManagerFactoryRef = "hotelEntityManager")
@EnableConfigurationProperties(HotelDataSourceProperties.class)
public class HotelBookingConfig {

    private final JpaVendorAdapter jpaVendorAdapter;
    private final HotelDataSourceProperties hotelDataSourceProperties;

    @Bean(name = "hotelDataSource", initMethod = "init", destroyMethod = "close")
    public DataSource hotelDataSource() {
        var xaDataSource = new AtomikosDataSourceBean();
        xaDataSource.setXaDataSourceClassName("org.postgresql.xa.PGXADataSource");
        xaDataSource.setUniqueResourceName(hotelDataSourceProperties.getXaResourceName());

        Properties properties = new Properties();
        properties.setProperty("user", hotelDataSourceProperties.getUser());
        properties.setProperty("password", hotelDataSourceProperties.getPassword());
        properties.setProperty("serverName", hotelDataSourceProperties.getServerName());
        properties.setProperty("portNumber", hotelDataSourceProperties.getPort().toString());
        properties.setProperty("databaseName", hotelDataSourceProperties.getDatabase());

        xaDataSource.setXaProperties(properties);

        return xaDataSource;
    }

    @Bean(name = "hotelEntityManager")
    public LocalContainerEntityManagerFactoryBean hotelEntityManager(DataSource hotelDataSource) {
        var properties = new HashMap<String, Object>();
        properties.put("hibernate.transaction.jta.platform", AtomikosJtaPlatform.class.getName());
        properties.put("javax.persistence.transactionType", "JTA");

        var entityManager = new LocalContainerEntityManagerFactoryBean();
        entityManager.setJtaDataSource(hotelDataSource);
        entityManager.setJpaVendorAdapter(jpaVendorAdapter);
        entityManager.setPackagesToScan("ua.stepess.microservices.pcdemo.domain.hotel");
        entityManager.setPersistenceUnitName("hotelPersistenceUnit");
        entityManager.setJpaPropertyMap(properties);
        return entityManager;
    }

}
