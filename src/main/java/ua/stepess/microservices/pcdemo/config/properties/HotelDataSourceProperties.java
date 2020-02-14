package ua.stepess.microservices.pcdemo.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "db.hotel")
public class HotelDataSourceProperties {
    private String url;
}
