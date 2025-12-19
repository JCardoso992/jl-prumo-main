package ao.prumo.obra.obramanagementservice.controller;

import ao.prumo.obra.obramanagementservice.entity.EtapaObra;
import ao.prumo.obra.obramanagementservice.entity.dto.request.EtapaObraRequest;
import ao.prumo.obra.obramanagementservice.entity.dto.response.EtapaObraResponse;
import ao.prumo.obra.obramanagementservice.service.EtapaObraService;
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
@RequestMapping(Constante.ROUTE + "/etapa-obra")
@Tag(name="Etapa da Obra", description="Gestão de obras/projetos, etapa da obra associadas a projetos arquitetonicos")
@RequiredArgsConstructor
public class EtapaObraController
{
    private EtapaObraService service;

    @Operation(summary = "Criar uma nova etapa da obra")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Etapa da obra criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<EtapaObraResponse> criarEtapaObra(@Valid @RequestBody EtapaObraRequest request) {
        EtapaObraResponse response = service.criar(request);
        return ResponseHttpBuilder.created("Etapa da obra criada com sucesso.", response);
    }

    @Operation(summary = "Listar etapas da obra (paginado)")
    @ApiResponse(responseCode = "200", description = "Lista de etapas da obra encontra.")
    @GetMapping("/pages/{id}")
    public ResponseEntity<HashMap> listaDeEtapaObras(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "12", required = false) int size,
            @PathVariable("id") Integer id
    ){
        Pageable pageable = PageRequest.of(page, size);
        Page<EtapaObraResponse> result = service.listar(pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("content", result.getContent());
        response.put("page", result.getNumber());
        response.put("size", result.getSize());
        response.put("totalElements", result.getTotalElements());
        response.put("totalPages", result.getTotalPages());

        return ResponseHttpBuilder.info("Lista de etapas recuperada com sucesso.", response);
    }

    @Operation(summary = "Lista de etapas da obra")
    @ApiResponse(responseCode = "200", description = "Lista de etapas da obra encontradas")
    @GetMapping("/{id}")
    public ResponseEntity<?> listaDeEtapaObras(@PathVariable("id") UUID id)
    {
        List<EtapaObra> etapaObraPage = service.findAll();
        EtapaObraResponse response = new EtapaObraResponse();
        return ResponseEntity.ok(response.listToDTO(etapaObraPage));
    }

    @Operation(summary = "Buscar etapa da obra por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Etapa da obra encontrada"),
            @ApiResponse(responseCode = "404", description = "Etapa da obra não encontrada")
    })
    @GetMapping("/pesquisar/{id}")
    public ResponseEntity<?> pesguisarEtapaObraById(@PathVariable("id") UUID id)
    {
        EtapaObraResponse response = service.buscarPorId(id);
        return ResponseHttpBuilder.info("Etapa da obra recuperada com sucesso.", response);
    }

    @Operation(summary = "Atualizar etapa da obra")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Etapa da obra atualizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Etapa da obra não encontrada"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<EtapaObraResponse> atualizarEtapaObra(@PathVariable UUID id, @Valid @RequestBody EtapaObraRequest request) {
         EtapaObraResponse response = service.atualizar(id, request);
        return ResponseHttpBuilder.info("Etapa da obra atualizada com sucesso.", response);
    }

    @Operation(summary = "Eliminar etapa da obra")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Etapa da obra eliminada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Etapa da obra não encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<EtapaObraResponse> eliminarEtapaObra(@PathVariable UUID id, @Valid @RequestBody EtapaObraRequest request) {
        EtapaObra etapaObraAtualizada = service.update(id, request.convertToEntity());
        EtapaObraResponse response = new EtapaObraResponse().convertToDTO(etapaObraAtualizada);
        return ResponseEntity.ok(response);
    }
}