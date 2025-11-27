package ao.prumo.obra.obramanagementservice.controller;

import ao.prumo.obra.obramanagementservice.entity.Cargo;
import ao.prumo.obra.obramanagementservice.entity.dto.request.CargoRequest;
import ao.prumo.obra.obramanagementservice.entity.dto.response.CargoResponse;
import ao.prumo.obra.obramanagementservice.service.CargoService;
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
@RequestMapping(Constante.ROUTE + "/cargo")
@Tag(name="Cargo", description="Gestão de obras/projetos, cargos associados")
@RequiredArgsConstructor
public class CargoController
{
    @Autowired
    private CargoService service;

    @Operation(summary = "Lista de cargos paginadas")
    @ApiResponse(responseCode = "200", description = "Lista de cargos encontrados páginados")
    @GetMapping("/pages/{id}")
    public ResponseEntity<HashMap> listaDeCargos(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "12", required = false) int size,
            @PathVariable("id") Integer id
    ){
        Pageable pageable = PageRequest.of(page, size);
        Page<Cargo> CargoPage = service.findAll(pageable);
        CargoResponse response = new CargoResponse();
        return ResponseEntity.ok(response.paginar(CargoPage));
    }

    @Operation(summary = "Lista de cargos")
    @ApiResponse(responseCode = "200", description = "Lista de cargos encontrados")
    @GetMapping("/{id}")
    public ResponseEntity<?> listaDeCargos(@PathVariable("id") UUID id)
    {
        List<Cargo> CargoPage = service.findAll();
        CargoResponse response = new CargoResponse();
        return ResponseEntity.ok(response.listToDTO(CargoPage));
    }

    @Operation(summary = "Pesquisar determinado cargo")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cargo encontrado"),
            @ApiResponse(responseCode = "404", description = "Cargo não encontrado")
    })
    @GetMapping("/cargo/{id}")
    public ResponseEntity<?> pesguisarCargoById(@PathVariable("id") UUID id)
    {
        Cargo Cargo = service.findById(id);
        CargoResponse response = new CargoResponse();
        return ResponseEntity.ok(response.convertToDTO(Cargo));
    }

    @Operation(summary = "Criar um novo cargo")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cargo criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CargoResponse> criarCargo(@Valid @RequestBody CargoRequest request) {
        Cargo novaCargo = service.save(request.convertToEntity());
        CargoResponse response = new CargoResponse().convertToDTO(novaCargo);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Atualizar cargo existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cargo atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cargo não encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CargoResponse> atualizarCargo(@PathVariable UUID id, @Valid @RequestBody CargoRequest request) {
        Cargo CargoAtualizada = service.update(id, request.convertToEntity());
        CargoResponse response = new CargoResponse().convertToDTO(CargoAtualizada);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Eliminar cargo existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cargo eliminado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cargo não encontrado")
    })
    @PutMapping("/eliminar/{id}")
    public ResponseEntity<CargoResponse> eliminarCargo(@PathVariable UUID id, @Valid @RequestBody CargoRequest request) {
        Cargo CargoAtualizada = service.update(id, request.convertToEntity());
        CargoResponse response = new CargoResponse().convertToDTO(CargoAtualizada);
        return ResponseEntity.ok(response);
    }
}
