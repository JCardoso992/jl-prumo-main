package ao.prumo.obra.obramanagementservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@SpringBootApplication
//@EnableJpaAuditing
@EnableJpaAuditing(auditorAwareRef = "auditorAwareImpl")
@EnableCaching
public class ObraManagementServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ObraManagementServiceApplication.class, args);
    }


    // 1. Configura o Swagger para usar o URL do Codespaces em vez de localhost
   /* @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .servers(List.of(
                    new Server().url("https://miniature-space-carnival-97q7xvpgv4g92pvj4-9093.app.github.dev")
                                .description("Ambiente remoto Codespaces")
                ));
    }*/

    // 2. Libera o CORS para que o Swagger (navegador) possa falar com a API
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*") 
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
            }
        };
    }

}
