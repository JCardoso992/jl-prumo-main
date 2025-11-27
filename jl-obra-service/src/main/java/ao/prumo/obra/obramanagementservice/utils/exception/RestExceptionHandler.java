package ao.prumo.obra.obramanagementservice.utils.exception;

import ao.prumo.obra.obramanagementservice.utils.http.ResponseHttp;
import ao.prumo.obra.obramanagementservice.utils.http.ResponseHttpBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.stream.Collectors;

/*
* Anotação chave que torna esta classe um componente global
* @ControllerAdvice: Transforma a classe em um "guardião" que observa todos os seus controllers.
* */
@ControllerAdvice
public class RestExceptionHandler
{

  // Manipulador para a exceção de recurso não encontrado
  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ResponseHttp> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
    // Usa o seu builder para criar a resposta de erro padronizada
    return ResponseHttpBuilder.build(
            HttpStatus.NOT_FOUND,
            ex.getMessage(), // Mensagem da exceção ("Registro não encontrado...")
            "URI: " + request.getDescription(false).replace("uri=", "")
    );
  }

  // Manipulador para erros de validação (@Valid)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ResponseHttp> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, WebRequest request) {
    // Pega todas as mensagens de erro de validação
    String errorDetails = ex.getBindingResult().getFieldErrors().stream()
            .map(fieldError -> "'" + fieldError.getField() + "': " + fieldError.getDefaultMessage())
            .collect(Collectors.joining(", "));

    return ResponseHttpBuilder.build(
            HttpStatus.BAD_REQUEST,
            "Erro de validação nos dados de entrada.",
            errorDetails
    );
  }

  // Manipulador genérico para qualquer outra exceção não tratada
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ResponseHttp> handleGlobalException(Exception ex, WebRequest request) {
    // É importante logar a exceção completa no servidor para depuração
    // log.error("Erro não tratado na requisição {}: {}", request.getDescription(false), ex);

    return ResponseHttpBuilder.build(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "Ocorreu um erro inesperado no servidor.",
            ex.getMessage() // Em produção, talvez seja melhor omitir ex.getMessage()
    );
  }
}