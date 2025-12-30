package ao.prumo.obra.obramanagementservice.controller;

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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;
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

    @Operation(summary = "Criar um novo cliente", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @io.swagger.v3.oas.annotations.media.Content(encoding = @io.swagger.v3.oas.annotations.media.Encoding(name = "request", contentType = "application/json"))))
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cliente criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    @PostMapping(value="/criar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> criarCliente(@Valid @RequestPart("request") ClienteRequest request,  @RequestPart("file") MultipartFile file) 
    {
        ClienteResponse response = service.criarCliente(request, file);
        return ResponseHttpBuilder.created("Cliente criado com sucesso.", response);
    }

    // =========================================================================
    // READ - LIST (PAGINADO)
    // =========================================================================

    @Operation(summary = "Listar clientes (paginado)")
    @ApiResponse(responseCode = "200", description = "Lista de clientes encontrado")
    @GetMapping("/pages")
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
    @GetMapping("/buscar/{id}")
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

    @Operation(summary = "Atualizar um cliente existente", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @io.swagger.v3.oas.annotations.media.Content(encoding = @io.swagger.v3.oas.annotations.media.Encoding(name = "request", contentType = "application/json"))))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> atualizarCliente( @PathVariable UUID id,
        @Valid @RequestPart("request") ClienteRequest request, @RequestPart("file") MultipartFile file ) 
    {
        ClienteResponse response = service.alterarCliente(id, request, file);
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