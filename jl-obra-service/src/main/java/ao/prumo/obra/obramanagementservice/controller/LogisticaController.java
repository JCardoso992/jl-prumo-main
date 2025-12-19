package ao.prumo.obra.obramanagementservice.controller;

import ao.prumo.obra.obramanagementservice.entity.Logistica;
import ao.prumo.obra.obramanagementservice.entity.dto.request.LogisticaRequest;
import ao.prumo.obra.obramanagementservice.entity.dto.response.LogisticaResponse;
import ao.prumo.obra.obramanagementservice.service.LogisticaService;
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
@RequestMapping(Constante.ROUTE + "/logistica")
@Tag(name="Logistica", description="Gestão de obras/projetos, logistica associada")
@RequiredArgsConstructor
public class LogisticaController
{
    private final LogisticaService service;

    // =========================================================================
    // CREATE
    // =========================================================================
    @Operation(summary = "Criar item de logística")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Item de logística criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    @PostMapping
    public ResponseEntity<?> criar(@Valid @RequestBody LogisticaRequest request) {
        LogisticaResponse response = service.criar(request);
        return ResponseHttpBuilder.created("Item de logística criado com sucesso.", response);
    }

    // =========================================================================
    // READ - LIST
    // =========================================================================
    @Operation(summary = "Listar itens de logística (paginado)")
    @ApiResponse(responseCode = "200", description = "Lista de itens de logística encontrado")
    @GetMapping
    public ResponseEntity<?> listar(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "12", required = false) int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<LogisticaResponse> result = service.listar(pageable);

        Map<String, Object> data = new HashMap<>();
        data.put("content", result.getContent());
        data.put("page", result.getNumber());
        data.put("size", result.getSize());
        data.put("totalElements", result.getTotalElements());
        data.put("totalPages", result.getTotalPages());

        return ResponseHttpBuilder.info("Lista de logística recuperada com sucesso.", data);
    }

    // =========================================================================
    // READ - BY ID
    // =========================================================================
    @Operation(summary = "Buscar item de logística por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Item de logística encontrado"),
            @ApiResponse(responseCode = "404", description = "Item de logística não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable UUID id) {
        LogisticaResponse response = service.buscarPorId(id);
        return ResponseHttpBuilder.info("Item de logística recuperado com sucesso.", response);
    }

    // =========================================================================
    // UPDATE
    // =========================================================================
    @Operation(summary = "Atualizar item de logística")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Item de logística atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Item de logística não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(
            @PathVariable UUID id,
            @Valid @RequestBody LogisticaRequest request) {
        LogisticaResponse response = service.atualizar(id, request);
        return ResponseHttpBuilder.info("Item de logística atualizado com sucesso.", response);
    }

    // =========================================================================
    // DELETE
    // =========================================================================
    @Operation(summary = "Excluir item de logística")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Item de logística eliminada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Item de logística não encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable UUID id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}