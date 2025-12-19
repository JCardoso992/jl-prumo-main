package ao.prumo.obra.obramanagementservice.controller;

import ao.prumo.obra.obramanagementservice.entity.Cliente;
import ao.prumo.obra.obramanagementservice.entity.dto.request.ClienteRequest;
import ao.prumo.obra.obramanagementservice.entity.dto.response.ClienteResponse;
import ao.prumo.obra.obramanagementservice.service.ClienteService;
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
@RequestMapping(Constante.ROUTE + "/cliente")
@Tag(name="Cliente", description="Gestão de obras/projetos, clientes associados")
@RequiredArgsConstructor
public class ClienteController
{
    private final ClienteService service;



    // =========================================================================
    // CREATE
    // =========================================================================

    @Operation(summary = "Criar um novo cliente")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cliente criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    @PostMapping
    public ResponseEntity<?> criarCliente(@Valid @RequestBody ClienteRequest request) {
        ClienteResponse response = service.criarCliente(request);
        return ResponseHttpBuilder.created("Cliente criado com sucesso.", response);
    }

    // =========================================================================
    // READ - LIST (PAGINADO)
    // =========================================================================

    @Operation(summary = "Listar clientes (paginado)")
    @ApiResponse(responseCode = "200", description = "Lista de clientes encontrada")
    @GetMapping
    public ResponseEntity<?> listaDeClientes(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "12", required = false) int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ClienteResponse> clientes = service.listarClientes(pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("content", clientes.getContent());
        response.put("page", clientes.getNumber());
        response.put("size", clientes.getSize());
        response.put("totalElements", clientes.getTotalElements());
        response.put("totalPages", clientes.getTotalPages());

        return ResponseHttpBuilder.info("Lista de clientes recuperada com sucesso.", response);
    }

    // =========================================================================
    // READ - BY ID
    // =========================================================================

    @Operation(summary = "Buscar cliente por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarClientePorId(@PathVariable UUID id) {
        // Você precisará de um método findById ou similar no ClienteService que retorne o DTO.
        // Assumindo que você o implementou:
        // ClienteResponse response = service.buscarPorId(id);

        // Para usar a estrutura existente que herda do BaseService:
        ClienteResponse response = service.buscarClientePorId(id);
        return ResponseHttpBuilder.info("Cliente recuperado com sucesso.", response);
    }

    // =========================================================================
    // UPDATE (PUT) - Atualizar Cliente
    // =========================================================================

    @Operation(summary = "Atualizar um cliente existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarCliente(
            @PathVariable UUID id,
            @Valid @RequestBody ClienteRequest request) {
        ClienteResponse response = service.alterarCliente(id, request);
        return ResponseHttpBuilder.info("Cliente atualizado com sucesso.", response);
    }

   // =========================================================================
    // DELETE
    // =========================================================================
    @Operation(summary = "Eliminar cliente")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Cliente eliminado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluirCliente(@PathVariable UUID id) {
        service.excluirCliente(id);
        return ResponseEntity.noContent().build();
    }
}