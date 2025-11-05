package hsf302.he187383.phudd.loginlicense.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Bean
    @ConfigurationProperties(prefix = "app.license")
    public LicenseApiProps licenseApiProps() {
        return new LicenseApiProps();
    }

    @Bean
    public RestTemplate restTemplate(LicenseApiProps p) {
        var f = new SimpleClientHttpRequestFactory();
        f.setConnectTimeout(p.getConnectTimeoutMs());
        f.setReadTimeout(p.getReadTimeoutMs());
        return new RestTemplate(f);
    }

    @Getter
    @Setter
    public static class LicenseApiProps {
        private String baseUrl;
        private int connectTimeoutMs = 3000;
        private int readTimeoutMs = 5000;
    }
}
