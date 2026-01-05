package ao.prumo.obra.obramanagementservice.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component("securityAuditorAware")
public class SecurityAuditorAware implements AuditorAware<UUID> {

    @Override
    public Optional<UUID> getCurrentAuditor() {
        // AQUI: Usamos apenas 'Authentication' do org.springframework.security.core
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() ||
                authentication instanceof AnonymousAuthenticationToken) {
            return Optional.empty();
        }

        // Verificamos se o principal é realmente um JWT antes de fazer o cast
        if (authentication.getPrincipal() instanceof Jwt jwt) {
            // O 'sub' do JWT no Supabase é o UUID do utilizador
            return Optional.of(UUID.fromString(jwt.getSubject()));
        }

        return Optional.empty();
    }
}