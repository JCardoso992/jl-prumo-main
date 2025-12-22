package ao.prumo.obra.obramanagementservice.controller;

import ao.prumo.obra.obramanagementservice.entity.dto.request.ContaOrganizacaoRequest;
import ao.prumo.obra.obramanagementservice.entity.dto.response.ContaOrganizacaoResponse;
import ao.prumo.obra.obramanagementservice.service.ContaOrganizacaoService;
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
@RequestMapping(Constante.ROUTE + "/conta-organizacao")
@Tag(name="ContaOrganizacao", description="Gestão de contas da organização")
@RequiredArgsConstructor
public class ContaOrganizacaoController
{
    private final ContaOrganizacaoService service;

    // =========================================================================
    // CREATE
    // =========================================================================
    @Operation(summary = "Criar uma nova conta da organização")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Conta criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    @PostMapping
    public ResponseEntity<?> criar(@Valid @RequestBody ContaOrganizacaoRequest request) {
        ContaOrganizacaoResponse response = service.criarContaOrganizacao(request);
        return ResponseHttpBuilder.created("Conta da organização criada com sucesso.", response);
    }

    // =========================================================================
    // READ - LIST (PAGINADO)
    // =========================================================================
    @Operation(summary = "Listar contas da organização (paginado)")
    @ApiResponse(responseCode = "200", description = "Lista encontrada")
    @GetMapping
    public ResponseEntity<?> listar(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "12", required = false) int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ContaOrganizacaoResponse> result = service.listar(pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("content", result.getContent());
        response.put("page", result.getNumber());
        response.put("size", result.getSize());
        response.put("totalElements", result.getTotalElements());
        response.put("totalPages", result.getTotalPages());

        return ResponseHttpBuilder.info("Lista de contas recuperada com sucesso.", response);
    }

    // =========================================================================
    // READ - BY ID
    // =========================================================================
    @Operation(summary = "Buscar conta da organização por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Conta encontrada"),
            @ApiResponse(responseCode = "404", description = "Conta não encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable UUID id) {
        ContaOrganizacaoResponse response = service.buscarPorId(id);
        return ResponseHttpBuilder.info("Conta recuperada com sucesso.", response);
    }

    // =========================================================================
    // UPDATE
    // =========================================================================
    @Operation(summary = "Atualizar conta da organização")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Conta atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Conta não encontrada"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(
            @PathVariable UUID id,
            @Valid @RequestBody ContaOrganizacaoRequest request) {
        ContaOrganizacaoResponse response = service.atualizar(id, request);
        return ResponseHttpBuilder.info("Conta atualizada com sucesso.", response);
    }

    // =========================================================================
    // DELETE
    // =========================================================================
    @Operation(summary = "Eliminar conta da organização")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Conta  eliminada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Conta não encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable UUID id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
