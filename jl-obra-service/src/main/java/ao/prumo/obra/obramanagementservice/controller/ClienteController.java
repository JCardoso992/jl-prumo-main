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
    // 1. CREATE (POST) - Criar Novo Cliente
    // =========================================================================

    @Operation(summary = "Cria um novo cliente no sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cliente criado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos (erro de validação).")
    })
    @PostMapping
    public ResponseEntity<?> criarCliente(@Valid @RequestBody ClienteRequest request) {
        ClienteResponse response = service.criarCliente(request);
        // Utiliza o ResponseHttpBuilder para retornar 201 CREATED
        return ResponseHttpBuilder.created("Cliente criado com sucesso.", response);
    }

    // =========================================================================
    // 2. READ (GET) - Listar Todos (Paginado)
    // =========================================================================

   /* @Operation(summary = "Lista todos os clientes com paginação")
    @ApiResponse(responseCode = "200", description = "Lista de clientes recuperada com sucesso.")
    @GetMapping
    public ResponseEntity<?> listaDeClientes(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "12", required = false) int size
    ) {
        Pageable pageable = PageRequest.of(page, size);

        // Assumindo que o BaseService.findAll(Pageable) retorna o HashMap paginado
        HashMap<String, Object> dadosPaginados = service.findAll(pageable);

        // Utiliza o ResponseHttpBuilder para retornar 200 OK
        return ResponseHttpBuilder.info("Lista de clientes recuperada.", dadosPaginados);
    }

    // 2. READ (GET) - Buscar por ID

    @Operation(summary = "Busca um cliente pelo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente encontrado."),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado.")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarClientePorId(@PathVariable UUID id) {
        // Você precisará de um método findById ou similar no ClienteService que retorne o DTO.
        // Assumindo que você o implementou:
        // ClienteResponse response = service.buscarPorId(id);

        // Para usar a estrutura existente que herda do BaseService:
        Cliente cliente = service.findById(id);
        ClienteResponse response = service.getClienteMapper().toResponse(cliente);

        // Utiliza o ResponseHttpBuilder para retornar 200 OK
        return ResponseHttpBuilder.info("Cliente recuperado com sucesso.", response);
    }*/

    // =========================================================================
    // 3. UPDATE (PUT) - Atualizar Cliente
    // =========================================================================

    @Operation(summary = "Atualiza os dados de um cliente existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos."),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado.")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarCliente(@PathVariable UUID id, @Valid @RequestBody ClienteRequest request) {
        ClienteResponse response = service.alterarCliente(id, request);
        // Utiliza o ResponseHttpBuilder para retornar 200 OK
        return ResponseHttpBuilder.info("Cliente atualizado com sucesso.", response);
    }

    // =========================================================================
    // 4. DELETE (DELETE) - Excluir Cliente
    // =========================================================================

    @Operation(summary = "Apaga um cliente pelo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Cliente apagado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado.")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluirCliente(@PathVariable UUID id) {
        service.excluirCliente(id);
        // Retorna o status 204 No Content, que é o padrão para DELETE bem-sucedido sem corpo.
        return ResponseEntity.noContent().build();
    }
}