package ao.prumo.obra.obramanagementservice.controller;

import ao.prumo.obra.obramanagementservice.entity.Obra;
import ao.prumo.obra.obramanagementservice.entity.dto.request.ObraRequest;
import ao.prumo.obra.obramanagementservice.entity.dto.response.ObraResponse;
import ao.prumo.obra.obramanagementservice.service.ObraService;
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
@RequestMapping(Constante.ROUTE + "/obra")
@Tag(name="Obra", description="Gestão de obras/projetos, obra associada")
@RequiredArgsConstructor
public class ObraController 
{
    private final ObraService service;

    // =========================================================================
    // CREATE
    // =========================================================================
    @Operation(summary = "Criar uma nova obra")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Obra criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    @PostMapping
    public ResponseEntity<?> criar(@Valid @RequestBody ObraRequest request) {
        ObraResponse response = service.criar(request);
        return ResponseHttpBuilder.created("Obra criada com sucesso.", response);
    }

    // =========================================================================
    // READ - LIST
    // =========================================================================
    @Operation(summary = "Listar obras (paginado)")
    @ApiResponse(responseCode = "200", description = "Lista de obra encontrado")
    @GetMapping
    public ResponseEntity<?> listar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ObraResponse> result = service.listar(pageable);

        Map<String, Object> data = new HashMap<>();
        data.put("content", result.getContent());
        data.put("page", result.getNumber());
        data.put("size", result.getSize());
        data.put("totalElements", result.getTotalElements());
        data.put("totalPages", result.getTotalPages());

        return ResponseHttpBuilder.info("Lista de obras recuperada com sucesso.", data);
    }

    // =========================================================================
    // READ - BY ID
    // =========================================================================
    @Operation(summary = "Buscar obra por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Obra encontrada"),
            @ApiResponse(responseCode = "404", description = "Obra não encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable UUID id) {
        ObraResponse response = service.buscarPorId(id);
        return ResponseHttpBuilder.info("Obra recuperada com sucesso.", response);
    }

    // =========================================================================
    // UPDATE
    // =========================================================================
    @Operation(summary = "Atualizar obra")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Obra atualizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Obra não encontrada"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(
            @PathVariable UUID id,
            @Valid @RequestBody ObraRequest request) {
        ObraResponse response = service.atualizar(id, request);
        return ResponseHttpBuilder.info("Obra atualizada com sucesso.", response);
    }

    // =========================================================================
    // DELETE
    // =========================================================================
    @Operation(summary = "Excluir obra")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Obra eliminada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Obra não encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable UUID id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }

}