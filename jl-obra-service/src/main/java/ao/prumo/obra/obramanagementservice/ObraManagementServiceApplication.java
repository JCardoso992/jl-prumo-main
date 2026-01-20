package ao.prumo.obra.obramanagementservice;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "securityAuditorAware")
@EnableCaching
public class ObraManagementServiceApplication {

    @Value("${app.cor.origins}")
    private String allowedOrigins;

    public static void main(String[] args) {
        SpringApplication.run(ObraManagementServiceApplication.class, args);
    }

     //Render: https://jl-prumo-main.onrender.com :  new Server().url("https://jl-prumo-main.onrender.com").description("Servidor Render"),
     //Codespaces: https://miniature-space-carnival-97q7xvpgv4g92pvj4-9093.app.github.dev
    // 1. Configura o Swagger para usar o URL do Codespaces em vez de localhost
     @Bean
     public OpenAPI customOpenAPI() {
         final String securitySchemeName = "bearerAuth";
         return new OpenAPI()
                 .servers(List.of(
                         new Server().url(allowedOrigins).description("Documentação da API Microservice JL-PRUMO Obra Management Service")
                 ))
                 .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                 .components(new Components()
                         .addSecuritySchemes(securitySchemeName,
                                 new SecurityScheme()
                                         .name(securitySchemeName)
                                         .type(SecurityScheme.Type.HTTP)
                                         .scheme("bearer")
                                         .bearerFormat("JWT")));
     }

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
