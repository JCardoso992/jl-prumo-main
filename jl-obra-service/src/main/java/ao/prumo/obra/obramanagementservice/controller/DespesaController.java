package ao.prumo.obra.obramanagementservice.controller;

import ao.prumo.obra.obramanagementservice.entity.dto.request.DespesaRequest;
import ao.prumo.obra.obramanagementservice.entity.dto.response.DespesaResponse;
import ao.prumo.obra.obramanagementservice.service.DespesaService;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(Constante.ROUTE + "/despesa")
@Tag(name="Despesa", description="Gestão de obras/projetos, despesas associadas")
@RequiredArgsConstructor
public class DespesaController
{
   
    private final DespesaService service;
    private final LogisticaService serviceLogistica;

     // =========================================================================
    // CREATE
    // =========================================================================

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
    @GetMapping("/pages")
    public ResponseEntity<?> listaDeDespesas(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "12", required = false) int size
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

 /*   @Operation(summary = "Lista de despesas")
    @ApiResponse(responseCode = "200", description = "Lista de despesas encontradas")
    @GetMapping("/buscar/{id}")
    public ResponseEntity<?> listaDeDespesas(@PathVariable("id") UUID id)
    {
        List<Despesa> DespesaPage = service.findAll();
        DespesaResponse response = new DespesaResponse();
        return ResponseEntity.ok(response.listToDTO(DespesaPage));
    }*/

    // =========================================================================
    // READ - BY ID
    // =========================================================================
    @Operation(summary = "Buscar despesa por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Despesa encontrada"),
            @ApiResponse(responseCode = "404", description = "Despesa não encontrada")
    })
    @GetMapping("/buscar/{id}")
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

    // =========================================================================
    // CREATE LOGISTICA
    // =========================================================================
    @Operation(summary = "Criar item de logística")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Item de logística criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    @PostMapping("/logistica")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> criar(@Valid @RequestBody LogisticaRequest request) {
        LogisticaResponse response = serviceLogistica.criar(request);
        return ResponseHttpBuilder.created("Item de logística criado com sucesso.", response);
    }

    // =========================================================================
    // READ - LIST
    // =========================================================================
    @Operation(summary = "Listar itens de logística (paginado)")
    @ApiResponse(responseCode = "200", description = "Lista de itens de logística encontrado")
    @GetMapping("/logistica/pages")
    public ResponseEntity<?> listar(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "12", required = false) int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<LogisticaResponse> result = serviceLogistica.listar(pageable);

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
    @GetMapping("/logistica/buscar/{id}")
    public ResponseEntity<?> buscarLogisticaPorId(@PathVariable UUID id) {
        LogisticaResponse response = serviceLogistica.buscarPorId(id);
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
    @PutMapping("/logistica/{id}")
    public ResponseEntity<?> atualizar(
            @PathVariable UUID id,
            @Valid @RequestBody LogisticaRequest request) {
        LogisticaResponse response = serviceLogistica.atualizar(id, request);
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
    @DeleteMapping("/logistica/{id}")
    public ResponseEntity<?> excluir(@PathVariable UUID id) {
        serviceLogistica.excluir(id);
        return ResponseEntity.noContent().build();
    }
}

