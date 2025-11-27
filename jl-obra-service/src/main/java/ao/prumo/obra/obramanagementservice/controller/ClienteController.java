package ao.prumo.obra.obramanagementservice.controller;

import ao.prumo.obra.obramanagementservice.entity.Cliente;
import ao.prumo.obra.obramanagementservice.entity.dto.request.ClienteRequest;
import ao.prumo.obra.obramanagementservice.entity.dto.response.ClienteResponse;
import ao.prumo.obra.obramanagementservice.service.ClienteService;
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
@RequestMapping(Constante.ROUTE + "/cliente")
@Tag(name="Cliente", description="Gestão de obras/projetos, clientes associados")
@RequiredArgsConstructor
public class ClienteController
{
    @Autowired
    private ClienteService service;

    @Operation(summary = "Lista de clientes paginados")
    @ApiResponse(responseCode = "200", description = "Lista de clientes encontrados páginados")
    @GetMapping("/pages/{id}")
    public ResponseEntity<HashMap> listaDeClientes(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "12", required = false) int size,
            @PathVariable("id") Integer id
    ){
        Pageable pageable = PageRequest.of(page, size);
        Page<Cliente> ClientePage = service.findAll(pageable);
        ClienteResponse response = new ClienteResponse();
        return ResponseEntity.ok(response.paginar(ClientePage));
    }

    @Operation(summary = "Lista de clientes")
    @ApiResponse(responseCode = "200", description = "Lista de clientes encontrados")
    @GetMapping("/{id}")
    public ResponseEntity<?> listaDeClientes(@PathVariable("id") UUID id)
    {
        List<Cliente> ClientePage = service.findAll();
        ClienteResponse response = new ClienteResponse();
        return ResponseEntity.ok(response.listToDTO(ClientePage));
    }

    @Operation(summary = "Pesquisar determinada cliente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente encontrada"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrada")
    })
    @GetMapping("/pesquisar/{id}")
    public ResponseEntity<?> pesguisarClienteById(@PathVariable("id") UUID id)
    {
        Cliente Cliente = service.findById(id);
        ClienteResponse response = new ClienteResponse();
        return ResponseEntity.ok(response.convertToDTO(Cliente));
    }

    @Operation(summary = "Criar uma novo cliente")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cliente criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ClienteResponse> criarCliente(@Valid @RequestBody ClienteRequest request) {
        Cliente novaCliente = service.save(request.convertToEntity());
        ClienteResponse response = new ClienteResponse().convertToDTO(novaCliente);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Atualizar cliente existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponse> atualizarCliente(@PathVariable UUID id, @Valid @RequestBody ClienteRequest request) {
        Cliente ClienteAtualizada = service.update(id, request.convertToEntity());
        ClienteResponse response = new ClienteResponse().convertToDTO(ClienteAtualizada);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Eliminar cliente existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente eliminado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    @PutMapping("/eliminar/{id}")
    public ResponseEntity<ClienteResponse> eliminarCliente(@PathVariable UUID id, @Valid @RequestBody ClienteRequest request) {
        Cliente ClienteAtualizada = service.update(id, request.convertToEntity());
        ClienteResponse response = new ClienteResponse().convertToDTO(ClienteAtualizada);
        return ResponseEntity.ok(response);
    }
}