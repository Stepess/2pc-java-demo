package ua.stepess.microservices.pcdemo.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "db.fly")
public class FlyDataSourceProperties {
    private String url;
}
