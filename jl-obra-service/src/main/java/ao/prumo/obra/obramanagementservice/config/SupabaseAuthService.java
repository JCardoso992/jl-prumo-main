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

    public UUID convidarUsuario(String email) 
    {
        //String url = supabaseUrl + "/auth/v1/admin/users";
        String url = supabaseUrl + "/auth/v1/admin/invite";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    
     // ADICIONE ESTA LINHA: O Supabase exige a apikey em um header separado
        headers.set("apikey", serviceRoleKey); 
    
        headers.setBearerAuth(serviceRoleKey);

        Map<String, Object> body = new HashMap<>();
        body.put("email", email);
        body.put("email_confirm", false); 
        //body.put("password", UUID.randomUUID().toString());

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
    
        // Dica: Adicione um try-catch para ver o erro detalhado se falhar
        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return UUID.fromString(response.getBody().get("id").toString());
            }
        } catch (org.springframework.web.client.HttpClientErrorException e) {
         System.err.println("Erro Supabase: " + e.getResponseBodyAsString());
         throw e;
        }
    
        throw new RuntimeException("Erro ao criar usuário no Supabase Auth");
    }
}