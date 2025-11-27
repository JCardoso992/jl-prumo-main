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
    @Autowired
    private DespesaService service;

    @Operation(summary = "Lista de despesas paginadas")
    @ApiResponse(responseCode = "200", description = "Lista de despesas encontradas páginadas")
    @GetMapping("/pages/{id}")
    public ResponseEntity<HashMap> listaDeDespesas(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "12", required = false) int size,
            @PathVariable("id") Integer id
    ){
        Pageable pageable = PageRequest.of(page, size);
        Page<Despesa> DespesaPage = service.findAll(pageable);
        DespesaResponse response = new DespesaResponse();
        return ResponseEntity.ok(response.paginar(DespesaPage));
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

    @Operation(summary = "Pesquisar determinada despesa")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Despesa encontrada"),
            @ApiResponse(responseCode = "404", description = "Despesa não encontrada")
    })
    @GetMapping("/Despesa/{id}")
    public ResponseEntity<?> pesguisarDespesaById(@PathVariable("id") UUID id)
    {
        Despesa Despesa = service.findById(id);
        DespesaResponse response = new DespesaResponse();
        return ResponseEntity.ok(response.convertToDTO(Despesa));
    }

    @Operation(summary = "Criar uma nova despesa")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Despesa criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<DespesaResponse> criarDespesa(@Valid @RequestBody DespesaRequest request) {
        Despesa novaDespesa = service.save(request.convertToEntity());
        DespesaResponse response = new DespesaResponse().convertToDTO(novaDespesa);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Atualizar despesa existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Despesa atualizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Despesa não encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<DespesaResponse> atualizarDespesa(@PathVariable UUID id, @Valid @RequestBody DespesaRequest request) {
        Despesa DespesaAtualizada = service.update(id, request.convertToEntity());
        DespesaResponse response = new DespesaResponse().convertToDTO(DespesaAtualizada);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Eliminar despesa existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Despesa eliminada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Despesa não encontrada")
    })
    @PutMapping("/eliminar/{id}")
    public ResponseEntity<DespesaResponse> eliminarDespesa(@PathVariable UUID id, @Valid @RequestBody DespesaRequest request) {
        Despesa DespesaAtualizada = service.update(id, request.convertToEntity());
        DespesaResponse response = new DespesaResponse().convertToDTO(DespesaAtualizada);
        return ResponseEntity.ok(response);
    }
}

