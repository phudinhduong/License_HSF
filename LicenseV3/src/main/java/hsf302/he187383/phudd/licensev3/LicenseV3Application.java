package hsf302.he187383.phudd.licensev3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class LicenseV3Application {

    public static void main(String[] args) {
        SpringApplication.run(LicenseV3Application.class, args);
    }

}
