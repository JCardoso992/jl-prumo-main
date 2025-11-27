package ao.prumo.obra.obramanagementservice.utils.http;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ResponseHttpBuilder
{

    // Método genérico para construir a resposta
    public static ResponseEntity<ResponseHttp> build(HttpStatus status, String mensagem, Object data) {
        ResponseHttp response = new ResponseHttp();
        response.setCode(status.value());
        response.setStatus(status);
        response.setMensagen(mensagem);
        response.setData(data); // Pode ser os detalhes do erro
        return ResponseEntity.status(status).body(response);
    }

    public static ResponseEntity<ResponseHttp> created(String mensagem, Object informacao)
    {
        return build(HttpStatus.CREATED, mensagem, informacao);
    }
    public static ResponseEntity<ResponseHttp> info(String mensagem, Object informacao)
    {
        return build(HttpStatus.OK, mensagem, informacao);
    }

    public static ResponseEntity<ResponseHttp> error(String mensagem, Object informacao)
    {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, mensagem, informacao);
    }

    public static ResponseEntity<ResponseHttp> warn(String mensagem, Object informacao)
    {
        return build(HttpStatus.NOT_FOUND, mensagem, informacao);
    }

    public static ResponseEntity<ResponseHttp> forbiden(String mensagem, Object informacao)
    {
        return build(HttpStatus.FORBIDDEN, mensagem, informacao);
    }

    public static Map<String, Object> toMap(ResponseEntity<?> entity)
    {
        ResponseHttp resposta = (ResponseHttp) entity.getBody();
        HashMap<String, Object> hash = new LinkedHashMap<>();
        hash.put("code", resposta.code);
        hash.put("message", resposta.mensagen);
        hash.put("status", resposta.status);
        hash.put("data", resposta.data);
        return hash;
    }
}
