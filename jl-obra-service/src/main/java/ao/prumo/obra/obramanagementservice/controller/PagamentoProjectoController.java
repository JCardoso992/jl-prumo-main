package ao.prumo.obra.obramanagementservice.controller;

import ao.prumo.obra.obramanagementservice.entity.PagamentoProjecto;
import ao.prumo.obra.obramanagementservice.entity.dto.request.PagamentoProjectoRequest;
import ao.prumo.obra.obramanagementservice.entity.dto.response.PagamentoProjectoResponse;
import ao.prumo.obra.obramanagementservice.service.PagamentoProjectoService;
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
@RequestMapping(Constante.ROUTE + "/pagamento-projecto")
@Tag(name="PagamentoProjecto", description="Gestão de pagamentos do projecto associado")
@RequiredArgsConstructor
public class PagamentoProjectoController  
{
    private final PagamentoProjectoService service;

    // =========================================================================
    // CREATE
    // =========================================================================
    @Operation(summary = "Criar um novo pagamento do projecto")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Pagamento do projecto criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    @PostMapping
    public ResponseEntity<?> criar(@Valid @RequestBody PagamentoProjectoRequest request)
    {
        PagamentoProjectoResponse response = service.criar(request);
        return ResponseHttpBuilder.created("Pagamento do projecto criado com sucesso.", response);
    }

    // =========================================================================
    // READ - LIST
    // =========================================================================
    @Operation(summary = "Listar pagamentos de projectos (paginado)")
    @GetMapping
    public ResponseEntity<?> listar(
        @RequestParam(name = "page", defaultValue = "0", required = false) int page,
        @RequestParam(name = "size", defaultValue = "12", required = false) int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PagamentoProjectoResponse> result = service.listar(pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("content", result.getContent());
        response.put("page", result.getNumber());
        response.put("size", result.getSize());
        response.put("totalElements", result.getTotalElements());
        response.put("totalPages", result.getTotalPages());

        return ResponseHttpBuilder.info("Lista de pagamentos recuperada com sucesso.", response);
    }

    // =========================================================================
    // READ - BY ID
    // =========================================================================
    @Operation(summary = "Buscar pagamento por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pagamento encontrada"),
        @ApiResponse(responseCode = "404", description = "Pagamento não encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable UUID id) 
    {
        PagamentoProjectoResponse response = service.buscarPorId(id);
        return ResponseHttpBuilder.info("Pagamento recuperado com sucesso.", response);
    }

    // =========================================================================
    // UPDATE
    // =========================================================================
    @Operation(summary = "Atualizar pagamento de projecto")
   @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pagamento atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Pagamento não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(
            @PathVariable UUID id,
            @Valid @RequestBody PagamentoProjectoRequest request) {
        PagamentoProjectoResponse response = service.atualizar(id, request);
        return ResponseHttpBuilder.info("Pagamento atualizado com sucesso.", response);
    }

    // =========================================================================
    // DELETE
    // =========================================================================
    @Operation(summary = "Excluir pagamento de projecto")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Pagamento eliminado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Pagamento não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable UUID id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }

}