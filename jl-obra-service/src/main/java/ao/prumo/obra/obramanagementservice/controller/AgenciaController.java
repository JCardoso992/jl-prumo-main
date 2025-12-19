package ao.prumo.obra.obramanagementservice.controller;

import ao.prumo.obra.obramanagementservice.entity.Agencia;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(Constante.ROUTE + "/agencia")
@Tag(name="Agencia", description="Gestão de obras/projetos, agências associadas")
@RequiredArgsConstructor
public class AgenciaController
{
    private AgenciaService service;

    // =========================================================================
    // 1. CREATE (POST) - Criar uma nova Agência
    // =========================================================================

    @Operation(summary = "Criar uma nova agência")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Agência criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    @PostMapping
    public ResponseEntity<?> criarAgencia(@Valid @RequestBody AgenciaRequest request) {
        AgenciaResponse response = service.criarAgencia(request);
        // Usa o builder para padronizar a resposta HTTP 201 CREATED
        return ResponseHttpBuilder.created("Agência criada com sucesso.", response);
    }

    // =========================================================================
    // 2. READ (GET) - Listar todas as Agências (Paginado)
    // =========================================================================

    @Operation(summary = "Listar todas as agências (com paginação)")
    @ApiResponse(responseCode = "200", description = "Lista de agências encontrada")
    @GetMapping
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
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarAgenciaPorId(@PathVariable UUID id) {
        AgenciaResponse response = service.buscarPorId(id);
        return ResponseHttpBuilder.info("Agência recuperada com sucesso.", response);
    }

    // =========================================================================
    // 3. UPDATE (PUT) - Atualizar uma Agência
    // =========================================================================

    @Operation(summary = "Atualizar agencia")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Agência atualizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Agência não encontrada"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarAgencia(@PathVariable UUID id, @Valid @RequestBody AgenciaRequest request) {
        AgenciaResponse response = service.alterarAgencia(id, request);
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
        // Retorna 204 No Content (sem corpo)
        return ResponseEntity.noContent().build();
    }
}
