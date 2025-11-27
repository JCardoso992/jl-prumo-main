package ao.prumo.obra.obramanagementservice.controller;

import ao.prumo.obra.obramanagementservice.entity.Agencia;
import ao.prumo.obra.obramanagementservice.entity.dto.request.AgenciaRequest;
import ao.prumo.obra.obramanagementservice.entity.dto.response.AgenciaResponse;
import ao.prumo.obra.obramanagementservice.service.AgenciaService;
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
@RequestMapping(Constante.ROUTE + "/agencia")
@Tag(name="Agencia", description="Gestão de obras/projetos, agências associadas")
@RequiredArgsConstructor
public class AgenciaController
{
    @Autowired
    private AgenciaService service;

    @Operation(summary = "Lista de agências paginadas")
    @ApiResponse(responseCode = "200", description = "Lista de agências encontradas páginadas")
    @GetMapping("/pages/{id}")
    public ResponseEntity<HashMap> listaDeAgencias(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "12", required = false) int size,
            @PathVariable("id") Integer id
    ){
        Pageable pageable = PageRequest.of(page, size);
        Page<Agencia> agenciaPage = service.findAll(pageable);
        AgenciaResponse response = new AgenciaResponse();
        return ResponseEntity.ok(response.paginar(agenciaPage));
    }

    @Operation(summary = "Lista de agências")
    @ApiResponse(responseCode = "200", description = "Lista de agências encontradas")
    @GetMapping("/{id}")
    public ResponseEntity<?> listaDeAgencias(@PathVariable("id") UUID id)
    {
        List<Agencia> agenciaPage = service.findAll();
        AgenciaResponse response = new AgenciaResponse();
        return ResponseEntity.ok(response.listToDTO(agenciaPage));
    }

    @Operation(summary = "Pesquisar determinada agência")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Agência encontrada"),
            @ApiResponse(responseCode = "404", description = "Agência não encontrada")
    })
    @GetMapping("/pesquisar/{id}")
    public ResponseEntity<?> pesguisarAgenciaById(@PathVariable("id") UUID id)
    {
        Agencia agencia = service.findById(id);
        AgenciaResponse response = new AgenciaResponse();
        return ResponseEntity.ok(response.convertToDTO(agencia));
    }

    @Operation(summary = "Criar uma nova agência")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Agência criada com sucesso"),
           @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<AgenciaResponse> criarAgencia(@Valid @RequestBody AgenciaRequest request) {
        Agencia novaAgencia = service.save(request.convertToEntity());
        AgenciaResponse response = new AgenciaResponse().convertToDTO(novaAgencia);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Atualizar agência existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Agência atualizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Agência não encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<AgenciaResponse> atualizarAgencia(@PathVariable UUID id, @Valid @RequestBody AgenciaRequest request) {
        Agencia agenciaAtualizada = service.update(id, request.convertToEntity());
        AgenciaResponse response = new AgenciaResponse().convertToDTO(agenciaAtualizada);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Eliminar agência existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Agência eliminada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Agência não encontrada")
    })
    @PutMapping("/eliminar/{id}")
    public ResponseEntity<AgenciaResponse> eliminarAgencia(@PathVariable UUID id, @Valid @RequestBody AgenciaRequest request) {
        Agencia agenciaAtualizada = service.update(id, request.convertToEntity());
        AgenciaResponse response = new AgenciaResponse().convertToDTO(agenciaAtualizada);
        return ResponseEntity.ok(response);
    }
}
