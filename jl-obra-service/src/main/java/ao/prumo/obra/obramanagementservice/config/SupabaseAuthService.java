package ao.prumo.obra.obramanagementservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SupabaseAuthService {
    @Value("${supabase.url}")
    private String supabaseUrl;
    @Value("${supabase.service-role-key}")
    private String serviceRoleKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public UUID convidarUsuario(String email) {
        String url = supabaseUrl + "/auth/v1/admin/users";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(serviceRoleKey);

        // O Supabase enviará o e-mail de confirmação automaticamente
        Map<String, Object> body = new HashMap<>();
        body.put("email", email);
        body.put("email_confirm", false); // false para enviar link de confirmação
        body.put("password", UUID.randomUUID().toString()); // Senha temporária aleatória

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return UUID.fromString(response.getBody().get("id").toString());
        }
        throw new RuntimeException("Erro ao criar usuário no Supabase Auth");
    }
}