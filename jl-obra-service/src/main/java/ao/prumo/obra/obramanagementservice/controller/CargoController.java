package ao.prumo.obra.obramanagementservice.controller;

import ao.prumo.obra.obramanagementservice.entity.dto.request.CargoRequest;
import ao.prumo.obra.obramanagementservice.entity.dto.response.CargoResponse;
import ao.prumo.obra.obramanagementservice.service.CargoService;
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
@RequestMapping(Constante.ROUTE + "/cargo")
@Tag(name="Cargo", description="Gestão de obras/projetos, cargos associados")
@RequiredArgsConstructor
public class CargoController
{
    private CargoService service;

    // =========================================================================
    // CREATE
    // =========================================================================
    @Operation(summary = "Criar um novo cargo")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cargo criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    @PostMapping
    public ResponseEntity<?> criarCargo(@Valid @RequestBody CargoRequest request) {
        CargoResponse response = service.criarCargo(request);
        return ResponseHttpBuilder.created("Cargo criado com sucesso.", response);
    }

    // =========================================================================
    // READ - LIST (PAGINADO)
    // =========================================================================
    @Operation(summary = "Listar cargos (paginado)")
    @ApiResponse(responseCode = "200", description = "Lista de cargos encontrada")
    @GetMapping
    public ResponseEntity<?> listarCargos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CargoResponse> cargos = service.listarCargos(pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("content", cargos.getContent());
        response.put("page", cargos.getNumber());
        response.put("size", cargos.getSize());
        response.put("totalElements", cargos.getTotalElements());
        response.put("totalPages", cargos.getTotalPages());

        return ResponseHttpBuilder.info("Lista de cargos recuperada com sucesso.", response);
    }

    // =========================================================================
    // READ - BY ID
    // =========================================================================
    @Operation(summary = "Buscar cargo por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cargo encontrado"),
            @ApiResponse(responseCode = "404", description = "Cargo não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarCargoPorId(@PathVariable UUID id) {
        CargoResponse response = service.buscarCargoPorId(id);
        return ResponseHttpBuilder.info("Cargo recuperado com sucesso.", response);
    }

    // =========================================================================
    // UPDATE
    // =========================================================================
    @Operation(summary = "Atualizar um cargo existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cargo atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cargo não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarCargo(
            @PathVariable UUID id,
            @Valid @RequestBody CargoRequest request) {
        CargoResponse response = service.alterarCargo(id, request);
        return ResponseHttpBuilder.info("Cargo atualizado com sucesso.", response);
    }

    // =========================================================================
    // DELETE
    // =========================================================================
    @Operation(summary = "Eliminar cargo")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Cargo eliminado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cargo não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluirCargo(@PathVariable UUID id) {
        service.excluirCargo(id);
        return ResponseEntity.noContent().build();
    }

}
