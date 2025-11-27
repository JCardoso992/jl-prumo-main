package ao.prumo.obra.obramanagementservice.controller;

import ao.prumo.obra.obramanagementservice.entity.DocumentoProjeto;
import ao.prumo.obra.obramanagementservice.entity.dto.request.DocumentoProjetoRequest;
import ao.prumo.obra.obramanagementservice.entity.dto.response.DocumentoProjetoResponse;
import ao.prumo.obra.obramanagementservice.service.DocumentoProjetoService;
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
import java.util.UUID;

@RestController
@RequestMapping(Constante.ROUTE + "/documento")
@Tag(name="Documento do Projeto", description="Gestão de obras/projetos, documento de projetos associados")
@RequiredArgsConstructor
public class DocumentoProjetoController
{
    @Autowired
    private DocumentoProjetoService service;

    @Operation(summary = "Lista de documentos de projeto paginados")
    @ApiResponse(responseCode = "200", description = "Lista de documentos de projeto encontrados páginados")
    @GetMapping("/pages/{organizacao}/{projeto}")
    public ResponseEntity<HashMap> listaDeDocumentoProjetos(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "12", required = false) int size,
            @PathVariable("organizacao") Integer organizacao,
            @PathVariable("projeto") Integer projeto
    ){
        Pageable pageable = PageRequest.of(page, size);
        Page<DocumentoProjeto> DocumentoProjetoPage = service.findAll(pageable);
        DocumentoProjetoResponse response = new DocumentoProjetoResponse();
        return ResponseEntity.ok(response.paginar(DocumentoProjetoPage));
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

    @Operation(summary = "Pesquisar determinada documento do projeto")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Documento de projeto encontrada"),
            @ApiResponse(responseCode = "404", description = "Documento de projeto não encontrada")
    })
    @GetMapping("/pesquisar/{id}")
    public ResponseEntity<?> pesguisarDocumentoProjetoById(@PathVariable("id") UUID id)
    {
        DocumentoProjeto DocumentoProjeto = service.findById(id);
        DocumentoProjetoResponse response = new DocumentoProjetoResponse();
        return ResponseEntity.ok(response.convertToDTO(DocumentoProjeto));
    }

    @Operation(summary = "Criar um novo documento de projeto")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Documento de projeto criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<DocumentoProjetoResponse> criarDocumentoProjeto(@Valid @RequestBody DocumentoProjetoRequest request) {
        DocumentoProjeto novaDocumentoProjeto = service.save(request.convertToEntity());
        DocumentoProjetoResponse response = new DocumentoProjetoResponse().convertToDTO(novaDocumentoProjeto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Atualizar documento de projeto existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Documento de projeto atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Documento de projeto não encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<DocumentoProjetoResponse> atualizarDocumentoProjeto(@PathVariable UUID id, @Valid @RequestBody DocumentoProjetoRequest request) {
        DocumentoProjeto DocumentoProjetoAtualizada = service.update(id, request.convertToEntity());
        DocumentoProjetoResponse response = new DocumentoProjetoResponse().convertToDTO(DocumentoProjetoAtualizada);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Eliminar documento de projeto existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Documento de projeto eliminado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Documento de projeto não encontrado")
    })
    @PutMapping("/eliminar/{id}")
    public ResponseEntity<DocumentoProjetoResponse> eliminarDocumentoProjeto(@PathVariable UUID id, @Valid @RequestBody DocumentoProjetoRequest request) {
        DocumentoProjeto DocumentoProjetoAtualizada = service.update(id, request.convertToEntity());
        DocumentoProjetoResponse response = new DocumentoProjetoResponse().convertToDTO(DocumentoProjetoAtualizada);
        return ResponseEntity.ok(response);
    }
}
