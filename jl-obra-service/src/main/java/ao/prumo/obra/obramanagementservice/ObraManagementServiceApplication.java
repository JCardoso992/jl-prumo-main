package ao.prumo.obra.obramanagementservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ObraManagementServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ObraManagementServiceApplication.class, args);
    }

}
