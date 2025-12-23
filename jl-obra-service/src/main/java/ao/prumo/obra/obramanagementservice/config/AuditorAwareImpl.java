package ao.prumo.obra.obramanagementservice.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;
import java.util.Optional;
import java.util.UUID;

@Component
public class AuditorAwareImpl implements AuditorAware<UUID> 
{
    /*
    * Retorna o nome do usuário atualmente autenticado.
    * No futuro, essa implementação pode ser expandida para buscar o usuário do contexto de segurança
    * (por exemplo, Keycloak ou Spring Security).
    */
    // Quando integrares o Keycloak:
    // Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    // if (authentication == null || !authentication.isAuthenticated()) {
   //     return Optional.empty();
   // // }
   // String username = authentication.getName();
   // return Optional.of(username);
   //OU
   //return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication().getName());
    @Override
    public Optional<UUID> getCurrentAuditor() {
        // Por enquanto, retornamos um usuário padrão. 
        // No futuro, aqui você buscaria o usuário do Keycloak/SecurityContext.
        //Para o caso de ser String : return Optional.of("SISTEMA_OBRA");
        //Para o caso de ser UUID
        return Optional.of(UUID.fromString("00000000-0000-0000-0000-000000000000")); 
        
    }
}