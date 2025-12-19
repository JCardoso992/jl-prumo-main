package ao.prumo.obra.obramanagementservice.controller;

import ao.prumo.obra.obramanagementservice.entity.Despesa;
import ao.prumo.obra.obramanagementservice.entity.dto.request.DespesaRequest;
import ao.prumo.obra.obramanagementservice.entity.dto.response.DespesaResponse;
import ao.prumo.obra.obramanagementservice.service.DespesaService;
import ao.prumo.obra.obramanagementservice.utils.globalConstantes.Constante;
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
@RequestMapping(Constante.ROUTE + "/despesa")
@Tag(name="Despesa", description="Gestão de obras/projetos, despesas associadas")
@RequiredArgsConstructor
public class DespesaController
{
   
    private DespesaService service;

    @Operation(summary = "Criar uma nova despesa")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Despesa criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> criarDespesa(@Valid @RequestBody DespesaRequest request) {
        DespesaResponse response = service.criarDespesa(request);
        return ResponseHttpBuilder.created("Despesa criada com sucesso.", response);
    }

    @Operation(summary = "Listar despesas (paginado)")
    @ApiResponse(responseCode = "200", description = "Lista encontrada")
    @GetMapping("/pages/{id}")
    public ResponseEntity<HashMap> listaDeDespesas(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "12", required = false) int size,
            @PathVariable("id") Integer id
    ){
        Pageable pageable = PageRequest.of(page, size);
        Page<DespesaResponse> result = service.listar(pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("content", result.getContent());
        response.put("page", result.getNumber());
        response.put("size", result.getSize());
        response.put("totalElements", result.getTotalElements());
        response.put("totalPages", result.getTotalPages());

        return ResponseHttpBuilder.info("Lista de despesas recuperada com sucesso.", response);
    }

    @Operation(summary = "Lista de despesas")
    @ApiResponse(responseCode = "200", description = "Lista de despesas encontradas")
    @GetMapping("/{id}")
    public ResponseEntity<?> listaDeDespesas(@PathVariable("id") UUID id)
    {
        List<Despesa> DespesaPage = service.findAll();
        DespesaResponse response = new DespesaResponse();
        return ResponseEntity.ok(response.listToDTO(DespesaPage));
    }

    // =========================================================================
    // READ - BY ID
    // =========================================================================
    @Operation(summary = "Buscar despesa por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Despesa encontrada"),
            @ApiResponse(responseCode = "404", description = "Despesa não encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable UUID id) {
        DespesaResponse response = service.buscarPorId(id);
        return ResponseHttpBuilder.info("Despesa recuperada com sucesso.", response);
    }


    @Operation(summary = "Atualizar despesa")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Despesa atualizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Despesa não encontrada"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarDespesa(@PathVariable UUID id, @Valid @RequestBody DespesaRequest request) {
        DespesaResponse response = service.atualizar(id, request);
        return ResponseHttpBuilder.info("Despesa atualizada com sucesso.", response);
    }

    @Operation(summary = "Eliminar despesa")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Despesa eliminada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Despesa não encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarDespesa(@PathVariable UUID id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}

