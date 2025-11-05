package hsf302.he187383.phudd.licensev3.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter @Setter
@Configuration
@ConfigurationProperties(prefix = "app.auth")
public class AuthProperties {
    private int sessionTtlSeconds = 28800;
    private int concurrentWindowSeconds = 600;
}
