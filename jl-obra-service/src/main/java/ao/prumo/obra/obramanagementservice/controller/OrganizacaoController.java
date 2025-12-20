import ao.prumo.obra.obramanagementservice.entity.Organizacao;
import ao.prumo.obra.obramanagementservice.entity.dto.request.OrganizacaoRequest;
import ao.prumo.obra.obramanagementservice.entity.dto.response.OrganizacaoResponse;
import ao.prumo.obra.obramanagementservice.service.OrganizacaoService;
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
@RequestMapping(Constante.ROUTE + "/organizacao")
@Tag(name="Organização", description="Gestão de organizações")
@RequiredArgsConstructor
public class OrganizacaoController  
{
    private final OrganizacaoService service;

    // =========================================================================
    // CREATE
    // =========================================================================
    @Operation(summary = "Criar uma nova organização")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Organização criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    @PostMapping
    public ResponseEntity<?> criar(@Valid @RequestBody ObraRequest request) 
    {
        OrganizacaoaResponse response = service.criar(request);
        return ResponseHttpBuilder.created("Organização criada com sucesso.", response);
    }

    // =========================================================================
    // READ - LIST
    // =========================================================================
    @Operation(summary = "Listar organizações (paginado)")
    @ApiResponse(responseCode = "200", description = "Lista de organização encontrado")
    @GetMapping
    public ResponseEntity<?> listar(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "12", required = false) int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<OrganizacaoResponse> result = service.listar(pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("content", result.getContent());
        response.put("page", result.getNumber());
        response.put("size", result.getSize());
        response.put("totalElements", result.getTotalElements());
        response.put("totalPages", result.getTotalPages());

        return ResponseHttpBuilder.info("Lista de organizações recuperada com sucesso.", response);
    }

    // =========================================================================
    // READ - BY ID
    // =========================================================================
    @Operation(summary = "Buscar organização por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Organização encontrado"),
            @ApiResponse(responseCode = "404", description = "Organização não encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable UUID id) 
    {
        OrganizacaoResponse response = service.buscarPorId(id);
        return ResponseHttpBuilder.info("Organização recuperada com sucesso.", response);
    }

    // =========================================================================
    // UPDATE
    // =========================================================================
    @Operation(summary = "Atualizar organização")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Organização atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Organização não encontrada"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(
        @PathVariable UUID id,
        @Valid @RequestBody OrganizacaoRequest request) 
    {
        OrganizacaoResponse response = service.atualizar(id, request);
        return ResponseHttpBuilder.info("Organização atualizada com sucesso.", response);
    }

    // =========================================================================
    // DELETE
    // =========================================================================
    @Operation(summary = "Excluir organização")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Organização eliminada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Organização não encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable UUID id) 
    {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }

}