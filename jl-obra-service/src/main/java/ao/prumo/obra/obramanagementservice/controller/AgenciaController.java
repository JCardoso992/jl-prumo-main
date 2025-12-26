package ao.prumo.obra.obramanagementservice.controller;

import ao.prumo.obra.obramanagementservice.entity.dto.request.AgenciaRequest;
import ao.prumo.obra.obramanagementservice.entity.dto.response.AgenciaResponse;
import ao.prumo.obra.obramanagementservice.service.AgenciaService;
import ao.prumo.obra.obramanagementservice.utils.globalConstantes.Constante;
import ao.prumo.obra.obramanagementservice.utils.http.ResponseHttpBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(Constante.ROUTE + "/agencia")
@Tag(name="Agencia", description="Gestão de obras/projetos, agências associadas")
@RequiredArgsConstructor
public class AgenciaController
{
    private final AgenciaService service;

    // =========================================================================
    // 1. CREATE (POST) - Criar uma nova Agência
    // 1.1. Swagger UI (OpenAPI)
    // @Operation(summary = "Criar uma nova agência", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @io.swagger.v3.oas.annotations.media.Content(encoding = @io.swagger.v3.oas.annotations.media.Encoding(name = "request", contentType = "application/json"))))
    //  @PostMapping(value="/criar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    //  1.2. Postman
    //  @PostMapping(value="/criar", consumes = "multipart/form-data")
    // =========================================================================

    @Operation(summary = "Criar uma nova agência", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @io.swagger.v3.oas.annotations.media.Content(encoding = @io.swagger.v3.oas.annotations.media.Encoding(name = "request", contentType = "application/json"))))
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Agência criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    @PostMapping(value="/criar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> criarAgencia(@Valid @RequestPart("request") AgenciaRequest request,
         @RequestPart("file") MultipartFile file )
    {
        AgenciaResponse response = service.criarAgencia(request, file);
        return ResponseHttpBuilder.created("Agência criada com sucesso.", response);
    }

    // =========================================================================
    // 2. READ (GET) - Listar todas as Agências (Paginado)
    // =========================================================================

    @Operation(summary = "Listar todas as agências (com paginação)")
    @ApiResponse(responseCode = "200", description = "Lista de agências encontrada")
    @GetMapping("/pages")
    public ResponseEntity<?> listaDeAgencias(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "12", required = false) int size
    ) {
         Pageable pageable = PageRequest.of(page, size);
         Page<AgenciaResponse> result = service.listar(pageable);

         Map<String, Object> response = new HashMap<>();
         response.put("content", result.getContent());
         response.put("page", result.getNumber());
         response.put("size", result.getSize());
         response.put("totalElements", result.getTotalElements());
         response.put("totalPages", result.getTotalPages());

         return ResponseHttpBuilder.info("Lista de agências recuperada com sucesso.", response);
    }

    // 2. READ (GET) - Buscar por ID

    @Operation(summary = "Buscar agência por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Agência encontrada"),
            @ApiResponse(responseCode = "404", description = "Agência não encontrada")
    })
    @GetMapping("/buscar/{id}")
    public ResponseEntity<?> buscarAgenciaPorId(@PathVariable UUID id) {
        AgenciaResponse response = service.buscarPorId(id);
        return ResponseHttpBuilder.info("Agência recuperada com sucesso.", response);
    }

    // =========================================================================
    // 3. UPDATE (PUT) - Atualizar uma Agência
    // =========================================================================

    @Operation(summary = "Atualizar agencia", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @io.swagger.v3.oas.annotations.media.Content(encoding = @io.swagger.v3.oas.annotations.media.Encoding(name = "request", contentType = "application/json"))))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Agência atualizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Agência não encontrada"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    @PutMapping(value="/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> atualizarAgencia(@PathVariable UUID id, @Valid @RequestPart("request") AgenciaRequest request,
                                              @RequestPart("file") MultipartFile file )
    {
        AgenciaResponse response = service.alterarAgencia(id, request, file);
        // Usa o builder para padronizar a resposta HTTP 200 OK
        return ResponseHttpBuilder.info("Agência atualizada com sucesso.", response);
    }

    // =========================================================================
    // 4. DELETE (DELETE) - Excluir uma Agência
    // =========================================================================

    @Operation(summary = "Eliminar agência")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Agência eliminada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Agência não encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluirAgencia(@PathVariable UUID id) {
        service.excluirAgencia(id);
        return ResponseEntity.noContent().build();
    }
}
