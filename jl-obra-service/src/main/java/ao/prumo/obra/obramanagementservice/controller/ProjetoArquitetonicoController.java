package ao.prumo.obra.obramanagementservice.controller;

import ao.prumo.obra.obramanagementservice.entity.dto.request.ProjetoArquitetonicoRequest;
import ao.prumo.obra.obramanagementservice.entity.dto.response.ProjetoArquitetonicoResponse;
import ao.prumo.obra.obramanagementservice.service.ProjetoArquitetonicoService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(Constante.ROUTE + "/projeto")
@Tag(name="Projeto Arquitetonico", description="Gestão de obras/projetos, projetos arquitetonico associados")
@RequiredArgsConstructor
public class ProjetoArquitetonicoController
{
    private final ProjetoArquitetonicoService service;

    @Operation(summary = "Criar uma novo projeto arquitetonico")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Projeto arquitetonico criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> criarProjetoArquitetonico(@Valid @RequestBody ProjetoArquitetonicoRequest request)
    {
        ProjetoArquitetonicoResponse response = service.criar(request);
        return ResponseHttpBuilder.created("Projeto arquitetônico criado com sucesso.", response);
    }

    @Operation(summary = "Listar projetos arquitetônicos (paginado)")
    @ApiResponse(responseCode = "200", description = "Lista de projetos arquitetonicos encontrado")
    @GetMapping("/pages/{id}")
    public ResponseEntity<?> listaDeProjetoArquitetonicos(
        @RequestParam(name = "page", defaultValue = "0", required = false) int page,
        @RequestParam(name = "size", defaultValue = "12", required = false) int size,
        @PathVariable("id") Integer id
    ){
        Pageable pageable = PageRequest.of(page, size);
        Page<ProjetoArquitetonicoResponse> result = service.listar(pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("content", result.getContent());
        response.put("page", result.getNumber());
        response.put("size", result.getSize());
        response.put("totalElements", result.getTotalElements());
        response.put("totalPages", result.getTotalPages());

        return ResponseHttpBuilder.info("Lista de projetos recuperada com sucesso.", response);
    }

    @Operation(summary = "Buscar projeto arquitetônico por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Projeto arquitetonico encontrado"),
        @ApiResponse(responseCode = "404", description = "Projeto arquitetonico não encontrado")
    })
    @GetMapping("/pesquisar/{id}")
    public ResponseEntity<?> pesguisarProjetoArquitetonicoById(@PathVariable("id") UUID id)
    {
       ProjetoArquitetonicoResponse response = service.buscarPorId(id);
       return ResponseHttpBuilder.info("Projeto arquitetônico recuperado com sucesso.", response);
    }

    

    @Operation(summary = "Atualizar Projeto arquitetonico")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Projeto arquitetonico atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Projeto arquitetonico não encontrado"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarProjetoArquitetonico(@PathVariable UUID id, @Valid @RequestBody ProjetoArquitetonicoRequest request)
    {
        ProjetoArquitetonicoResponse response = service.atualizar(id, request);
        return ResponseHttpBuilder.info("Projeto arquitetônico atualizado com sucesso.", response);
    }

    @Operation(summary = "Eliminar Projeto arquitetonico")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Projeto arquitetonico eliminado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Projeto arquitetonico não encontrado")
    })
    @PutMapping("/eliminar/{id}")
    public ResponseEntity<ProjetoArquitetonicoResponse> eliminarprojetoArquitetonico(@PathVariable UUID id)
    {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
