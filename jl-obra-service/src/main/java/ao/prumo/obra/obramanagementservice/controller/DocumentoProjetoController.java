package ao.prumo.obra.obramanagementservice.controller;

import ao.prumo.obra.obramanagementservice.entity.dto.request.DocumentoProjetoRequest;
import ao.prumo.obra.obramanagementservice.entity.dto.response.DocumentoProjetoResponse;
import ao.prumo.obra.obramanagementservice.service.DocumentoProjetoService;
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
@RequestMapping(Constante.ROUTE + "/documento-projeto")
@Tag(name="DocumentoProjeto", description="Gestão de obras/projetos, documento de projetos associados")
@RequiredArgsConstructor
public class DocumentoProjetoController
{

    private final DocumentoProjetoService service;

    @Operation(summary = "Criar um novo documento de projeto")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Documento de projeto criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> criarDocumentoProjeto(@Valid @RequestBody DocumentoProjetoRequest request) {
        DocumentoProjetoResponse response = service.criarDocumentoProjeto(request);
        return ResponseHttpBuilder.created("Documento do projeto criado com sucesso.", response);
    }

    @Operation(summary = "Listar documentos do projeto (paginado)")
    @ApiResponse(responseCode = "200", description = "Lista encontrada")
    @GetMapping("/pages/{organizacao}/{projeto}")
    public ResponseEntity<?> listaDeDocumentoProjetos(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "12", required = false) int size,
            @PathVariable("organizacao") Integer organizacao,
            @PathVariable("projeto") Integer projeto
    ){
        Pageable pageable = PageRequest.of(page, size);
        Page<DocumentoProjetoResponse> result = service.listar(pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("content", result.getContent());
        response.put("page", result.getNumber());
        response.put("size", result.getSize());
        response.put("totalElements", result.getTotalElements());
        response.put("totalPages", result.getTotalPages());

        return ResponseHttpBuilder.info("Lista de documentos recuperada com sucesso.", response);
    }

    /* @Operation(summary = "Lista de agências")
    @ApiResponse(responseCode = "200", description = "Lista de agências encontradas")
    @GetMapping("/{id}")
    public ResponseEntity<?> listaDeDocumentoProjetos(@PathVariable("id") UUID id)
    {
        List<DocumentoProjeto> DocumentoProjetoPage = service.findAll();
        DocumentoProjetoResponse response = new DocumentoProjetoResponse();
        return ResponseEntity.ok(response.listToDTO(DocumentoProjetoPage));
    }*/

    @Operation(summary = "Buscar documento do projeto por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Documento de projeto encontrada"),
            @ApiResponse(responseCode = "404", description = "Documento de projeto não encontrada")
    })
    @GetMapping("/pesquisar/{id}")
    public ResponseEntity<?> pesguisarDocumentoProjetoById(@PathVariable("id") UUID id)
    {
        DocumentoProjetoResponse response = service.buscarPorId(id);
        return ResponseHttpBuilder.info("Documento recuperado com sucesso.", response);
    }

    

    @Operation(summary = "Atualizar documento de projeto")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Documento de projeto atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Documento de projeto não encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarDocumentoProjeto(@PathVariable UUID id, @Valid @RequestBody DocumentoProjetoRequest request) {
        DocumentoProjetoResponse response = service.atualizar(id, request);
        return ResponseHttpBuilder.info("Documento atualizado com sucesso.", response);
    }

    @Operation(summary = "Eliminar documento de projeto")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Documento de projeto eliminado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Documento de projeto não encontrado")
    })
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarDocumentoProjeto(@PathVariable UUID id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
