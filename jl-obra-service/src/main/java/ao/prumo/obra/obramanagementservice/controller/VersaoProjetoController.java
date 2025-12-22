package ao.prumo.obra.obramanagementservice.controller;

import ao.prumo.obra.obramanagementservice.entity.dto.request.VersaoProjetoRequest;
import ao.prumo.obra.obramanagementservice.entity.dto.response.VersaoProjetoResponse;
import ao.prumo.obra.obramanagementservice.service.VersaoProjetoService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(Constante.ROUTE + "/versao-projeto")
@Tag(name="VersaoProjeto", description="Gestão de versões de projetos")
@RequiredArgsConstructor
public class VersaoProjetoController 
{
    private final VersaoProjetoService service;

    // =========================================================================
    // CREATE
    // =========================================================================
    @Operation(summary = "Criar versão de projeto")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Versão de projeto criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    @PostMapping
    public ResponseEntity<?> criar(@Valid @RequestBody VersaoProjetoRequest request) 
    {
        VersaoProjetoResponse response = service.criar(request);
        return ResponseHttpBuilder.created("Versão de projeto criada com sucesso.", response);
    }

    // =========================================================================
    // READ - LIST
    // =========================================================================
    @Operation(summary = "Listar versões de projeto (paginado)")
    @ApiResponse(responseCode = "200", description = "Lista de versões de projeto encontrada")
    @GetMapping
    public ResponseEntity<?> listar(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "12", required = false) int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<VersaoProjetoResponse> result = service.listar(pageable);

        Map<String, Object> data = new HashMap<>();
        data.put("content", result.getContent());
        data.put("page", result.getNumber());
        data.put("size", result.getSize());
        data.put("totalElements", result.getTotalElements());
        data.put("totalPages", result.getTotalPages());

        return ResponseHttpBuilder.info("Lista de versões recuperada com sucesso.", data);
    }

    // =========================================================================
    // READ - BY ID
    // =========================================================================
    @Operation(summary = "Buscar versão de projeto por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Versão de projeto encontrada"),
            @ApiResponse(responseCode = "404", description = "Versão de projeto não encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable UUID id) {
        VersaoProjetoResponse response = service.buscarPorId(id);
        return ResponseHttpBuilder.info("Versão de projeto recuperada com sucesso.", response);
    }

    // =========================================================================
    // UPDATE
    // =========================================================================
    @Operation(summary = "Atualizar versão de projeto")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Versão de projeto atualizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Versão de projeto não encontrada"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(
            @PathVariable UUID id,
            @Valid @RequestBody VersaoProjetoRequest request) {
        VersaoProjetoResponse response = service.atualizar(id, request);
        return ResponseHttpBuilder.info("Versão de projeto atualizada com sucesso.", response);
    }

    // =========================================================================
    // DELETE
    // =========================================================================
    @Operation(summary = "Excluir versão de projeto")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Versão de projeto eliminada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Versão de projeto não encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable UUID id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }

}