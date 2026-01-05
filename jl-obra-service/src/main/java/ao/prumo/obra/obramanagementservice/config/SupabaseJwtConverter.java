package ao.prumo.obra.obramanagementservice.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class SupabaseJwtConverter implements Converter<Jwt, AbstractAuthenticationToken>
{

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        Map<String, Object> appMetadata = jwt.getClaimAsMap("app_metadata");
        String role = (String) appMetadata.get("role");
        Boolean ativo = (Boolean) appMetadata.get("ativo");

        // Se o utilizador não estiver ativo, limpamos as autoridades (bloqueio)
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        if (ativo != null && ativo && role != null) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
        }

        return new JwtAuthenticationToken(jwt, authorities);
    }
}