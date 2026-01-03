package ao.prumo.obra.obramanagementservice.controller;

import ao.prumo.obra.obramanagementservice.entity.dto.request.ProjetoArquitetonicoRequest;
import ao.prumo.obra.obramanagementservice.entity.dto.response.ProjetoArquitetonicoResponse;
import ao.prumo.obra.obramanagementservice.entity.dto.request.PagamentoProjectoRequest;
import ao.prumo.obra.obramanagementservice.entity.dto.response.PagamentoProjectoResponse;
import ao.prumo.obra.obramanagementservice.entity.dto.request.VersaoProjetoRequest;
import ao.prumo.obra.obramanagementservice.entity.dto.response.VersaoProjetoResponse;
import ao.prumo.obra.obramanagementservice.entity.dto.request.ObraRequest;
import ao.prumo.obra.obramanagementservice.entity.dto.response.ObraResponse;
import ao.prumo.obra.obramanagementservice.entity.dto.request.EtapaObraRequest;
import ao.prumo.obra.obramanagementservice.entity.dto.response.EtapaObraResponse;
import ao.prumo.obra.obramanagementservice.entity.dto.request.DocumentoProjetoRequest;
import ao.prumo.obra.obramanagementservice.entity.dto.response.DocumentoProjetoResponse;
import ao.prumo.obra.obramanagementservice.service.DocumentoProjetoService;
import ao.prumo.obra.obramanagementservice.service.EtapaObraService;
import ao.prumo.obra.obramanagementservice.service.ObraService;
import ao.prumo.obra.obramanagementservice.service.PagamentoProjectoService;
import ao.prumo.obra.obramanagementservice.service.ProjetoArquitetonicoService;
import ao.prumo.obra.obramanagementservice.service.VersaoProjetoService;
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

    private final PagamentoProjectoService servicePagamento;

    private final ObraService serviceObra;

    private final VersaoProjetoService serviceVersao;

    private final EtapaObraService serviceEtapa;

    private final DocumentoProjetoService serviceDocumento;

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
    @GetMapping("/pages")
    public ResponseEntity<?> listaDeProjetoArquitetonicos(
        @RequestParam(name = "page", defaultValue = "0", required = false) int page,
        @RequestParam(name = "size", defaultValue = "12", required = false) int size
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
    @GetMapping("/buscar/{id}")
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
    @DeleteMapping("/{id}")
    public ResponseEntity<ProjetoArquitetonicoResponse> eliminarprojetoArquitetonico(@PathVariable UUID id)
    {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }

    // =========================================================================
    // CREATE PAGAMENTO DE PROJECTO
    // =========================================================================
    @Operation(summary = "Criar um novo pagamento do projecto")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Pagamento do projecto criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    @PostMapping("/pagamento")
    @ResponseStatus(HttpStatus.CREATED)    
    public ResponseEntity<?> criar(@Valid @RequestBody PagamentoProjectoRequest request)
    {
        PagamentoProjectoResponse response = servicePagamento.criar(request);
        return ResponseHttpBuilder.created("Pagamento do projecto criado com sucesso.", response);
    }

    // =========================================================================
    // READ - LIST PAGAMENTO DE PROJECTO
    // =========================================================================
    @Operation(summary = "Listar pagamentos de projectos (paginado)")
    @GetMapping("/pagamento/pages/{id}")
    public ResponseEntity<?> listarPagamento(
        @RequestParam(name = "page", defaultValue = "0", required = false) int page,
        @RequestParam(name = "size", defaultValue = "12", required = false) int size,
        @PathVariable UUID id
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PagamentoProjectoResponse> result = servicePagamento.listar(pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("content", result.getContent());
        response.put("page", result.getNumber());
        response.put("size", result.getSize());
        response.put("totalElements", result.getTotalElements());
        response.put("totalPages", result.getTotalPages());

        return ResponseHttpBuilder.info("Lista de pagamentos recuperada com sucesso.", response);
    }

    // =========================================================================
    // READ - BY ID PAGAMENTO DE PROJECTO
    // =========================================================================
    @Operation(summary = "Buscar pagamento por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pagamento encontrada"),
        @ApiResponse(responseCode = "404", description = "Pagamento não encontrada")
    })
    @GetMapping("/pagamento/buscar/{id}")
    public ResponseEntity<?> buscarPorIdPagamento(@PathVariable UUID id) 
    {
        PagamentoProjectoResponse response = servicePagamento.buscarPorId(id);
        return ResponseHttpBuilder.info("Pagamento recuperado com sucesso.", response);
    }

    // =========================================================================
    // UPDATE PAGAMENTO DE PROJECTO
    // =========================================================================
    @Operation(summary = "Atualizar pagamento de projecto")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pagamento atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Pagamento não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    @PutMapping("/pagamento/{id}")
    public ResponseEntity<?> atualizarPagamento(
            @PathVariable UUID id,
            @Valid @RequestBody PagamentoProjectoRequest request) {
        PagamentoProjectoResponse response = servicePagamento.atualizar(id, request);
        return ResponseHttpBuilder.info("Pagamento atualizado com sucesso.", response);
    }

    // =========================================================================
    // DELETE PAGAMENTO DE PROJECTO
    // =========================================================================
    @Operation(summary = "Excluir pagamento de projecto")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Pagamento eliminado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Pagamento não encontrado")
    })
    @DeleteMapping("/pagamento/{id}")
    public ResponseEntity<?> excluirPagamento(@PathVariable UUID id) {
        servicePagamento.excluir(id);
        return ResponseEntity.noContent().build();
    }

    // =========================================================================
    // CREATE OBRA DO PROJECTO
    // =========================================================================
    @Operation(summary = "Criar uma nova obra")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Obra criada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    @PostMapping("/obra-projecto")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> criarObra(@Valid @RequestBody ObraRequest request) {
        ObraResponse response = serviceObra.criar(request);
        return ResponseHttpBuilder.created("Obra criada com sucesso.", response);
    }

    // =========================================================================
    // READ - LIST OBRA DO PROJECTO
    // =========================================================================
    @Operation(summary = "Listar obras (paginado)")
    @ApiResponse(responseCode = "200", description = "Lista de obra encontrado")
    @GetMapping("/obra-projecto/pages/{id}")
    public ResponseEntity<?> listarObra(
        @RequestParam(name = "page", defaultValue = "0", required = false) int page,
        @RequestParam(name = "size", defaultValue = "12", required = false) int size,
        @PathVariable UUID id
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ObraResponse> result = serviceObra.listar(pageable);

        Map<String, Object> data = new HashMap<>();
        data.put("content", result.getContent());
        data.put("page", result.getNumber());
        data.put("size", result.getSize());
        data.put("totalElements", result.getTotalElements());
        data.put("totalPages", result.getTotalPages());

        return ResponseHttpBuilder.info("Lista de obras recuperada com sucesso.", data);
    }

    // =========================================================================
    // READ - BY ID OBRA DO PROJECTO
    // =========================================================================
    @Operation(summary = "Buscar obra por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Obra encontrada"),
        @ApiResponse(responseCode = "404", description = "Obra não encontrada")
    })
    @GetMapping("/obra-projecto/buscar/{id}")
    public ResponseEntity<?> buscarPorIdObra(@PathVariable UUID id) {
        ObraResponse response = serviceObra.buscarPorId(id);
        return ResponseHttpBuilder.info("Obra recuperada com sucesso.", response);
    }

    // =========================================================================
    // UPDATE OBRA DO PROJECTO
    // =========================================================================
    @Operation(summary = "Atualizar obra")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Obra atualizada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Obra não encontrada"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    @PutMapping("/obra-projecto/{id}")
    public ResponseEntity<?> atualizarObra(
            @PathVariable UUID id,
            @Valid @RequestBody ObraRequest request) {
        ObraResponse response = serviceObra.atualizar(id, request);
        return ResponseHttpBuilder.info("Obra atualizada com sucesso.", response);
    }

    // =========================================================================
    // DELETE OBRA DO PROJECTO
    // =========================================================================
    @Operation(summary = "Excluir obra")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Obra eliminada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Obra não encontrada")
    })
    @DeleteMapping("/obra-projecto/{id}")
    public ResponseEntity<?> excluirObra(@PathVariable UUID id) {
        serviceObra.excluir(id);
        return ResponseEntity.noContent().build();
    }

    // =========================================================================
    // CREATE VERSÃO DO PROJECTO
    // =========================================================================
    @Operation(summary = "Criar versão de projeto")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Versão de projeto criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    @PostMapping("/versao-projeto")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> criar(@Valid @RequestBody VersaoProjetoRequest request) 
    {
        VersaoProjetoResponse response = serviceVersao.criar(request);
        return ResponseHttpBuilder.created("Versão de projeto criada com sucesso.", response);
    }

    // =========================================================================
    // READ - LIST VERSÃO DO PROJECTO
    // =========================================================================
    @Operation(summary = "Listar versões de projeto (paginado)")
    @ApiResponse(responseCode = "200", description = "Lista de versões de projeto encontrada")
    @GetMapping("/versao-projeto/{id}")
    public ResponseEntity<?> listar(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "12", required = false) int size,
            @PathVariable UUID id
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<VersaoProjetoResponse> result = serviceVersao.listar(pageable);

        Map<String, Object> data = new HashMap<>();
        data.put("content", result.getContent());
        data.put("page", result.getNumber());
        data.put("size", result.getSize());
        data.put("totalElements", result.getTotalElements());
        data.put("totalPages", result.getTotalPages());

        return ResponseHttpBuilder.info("Lista de versões recuperada com sucesso.", data);
    }

    // =========================================================================
    // READ - BY ID VERSÃO DO PROJECTO
    // =========================================================================
    @Operation(summary = "Buscar versão de projeto por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Versão de projeto encontrada"),
            @ApiResponse(responseCode = "404", description = "Versão de projeto não encontrada")
    })
    @GetMapping("/versao-projeto/buscar/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable UUID id) {
        VersaoProjetoResponse response = serviceVersao.buscarPorId(id);
        return ResponseHttpBuilder.info("Versão de projeto recuperada com sucesso.", response);
    }

    // =========================================================================
    // UPDATE VERSÃO DO PROJECTO
    // =========================================================================
    @Operation(summary = "Atualizar versão de projeto")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Versão de projeto atualizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Versão de projeto não encontrada"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    @PutMapping("/versao-projeto/{id}")
    public ResponseEntity<?> atualizar(
            @PathVariable UUID id,
            @Valid @RequestBody VersaoProjetoRequest request) {
        VersaoProjetoResponse response = serviceVersao.atualizar(id, request);
        return ResponseHttpBuilder.info("Versão de projeto atualizada com sucesso.", response);
    }

    // =========================================================================
    // DELETE VERSÃO DO PROJECTO
    // =========================================================================
    @Operation(summary = "Excluir versão de projeto")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Versão de projeto eliminada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Versão de projeto não encontrada")
    })
    @DeleteMapping("/versao-projeto/{id}")
    public ResponseEntity<?> excluir(@PathVariable UUID id) {
        serviceVersao.excluir(id);
        return ResponseEntity.noContent().build();
    }

    // =========================================================================
    // CREATE ETAPA DA OBRA
    // =========================================================================
    @Operation(summary = "Criar uma nova etapa da obra")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Etapa da obra criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    @PostMapping("/etapa-obra")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> criarEtapaObra(@Valid @RequestBody EtapaObraRequest request) {
        EtapaObraResponse response = serviceEtapa.criar(request);
        return ResponseHttpBuilder.created("Etapa da obra criada com sucesso.", response);
    }

    // =========================================================================
    // READ - LIST ETAPA DA OBRA
    // =========================================================================
    @Operation(summary = "Listar etapas da obra (paginado)")
    @ApiResponse(responseCode = "200", description = "Lista de etapas da obra encontra.")
    @GetMapping("/etapa-obra/{id}")
    public ResponseEntity<?> listaDeEtapaObras(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "12", required = false) int size,
            @PathVariable("id") Integer id
    ){
        Pageable pageable = PageRequest.of(page, size);
        Page<EtapaObraResponse> result = serviceEtapa.listar(pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("content", result.getContent());
        response.put("page", result.getNumber());
        response.put("size", result.getSize());
        response.put("totalElements", result.getTotalElements());
        response.put("totalPages", result.getTotalPages());

        return ResponseHttpBuilder.info("Lista de etapas recuperada com sucesso.", response);
    }

   /* @Operation(summary = "Lista de etapas da obra")
    @ApiResponse(responseCode = "200", description = "Lista de etapas da obra encontradas")
    @GetMapping("/{id}")
    public ResponseEntity<?> listaDeEtapaObras(@PathVariable("id") UUID id)
    {
        List<EtapaObra> etapaObraPage = service.findAll();
        EtapaObraResponse response = new EtapaObraResponse();
        return ResponseEntity.ok(response.listToDTO(etapaObraPage));
    }*/

    // =========================================================================
    // READ - BY ID ETAPA DA OBRA
    // =========================================================================    
    @Operation(summary = "Buscar etapa da obra por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Etapa da obra encontrada"),
            @ApiResponse(responseCode = "404", description = "Etapa da obra não encontrada")
    })
    @GetMapping("/etapa-obra/buscar/{id}")
    public ResponseEntity<?> pesguisarEtapaObraById(@PathVariable("id") UUID id)
    {
        EtapaObraResponse response = serviceEtapa.buscarPorId(id);
        return ResponseHttpBuilder.info("Etapa da obra recuperada com sucesso.", response);
    }

    // =========================================================================
    // UPDATE ETAPA DA OBRA
    // =========================================================================
    @Operation(summary = "Atualizar etapa da obra")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Etapa da obra atualizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Etapa da obra não encontrada"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    @PutMapping("/etapa-obra/{id}")
    public ResponseEntity<?> atualizarEtapaObra(@PathVariable UUID id, @Valid @RequestBody EtapaObraRequest request) {
        EtapaObraResponse response = serviceEtapa.atualizar(id, request);
        return ResponseHttpBuilder.info("Etapa da obra atualizada com sucesso.", response);
    }


    // =========================================================================
    // DELETE ETAPA DA OBRA
    // =========================================================================
    @Operation(summary = "Eliminar etapa da obra")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Etapa da obra eliminada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Etapa da obra não encontrada")
    })
    @DeleteMapping("/etapa-obra/{id}")
    public ResponseEntity<?> eliminarEtapaObra(@PathVariable UUID id) {
        serviceEtapa.excluir(id);
        return ResponseEntity.noContent().build();
    }


    // =========================================================================
    // CREATE DOCUMENTO DO PROJECTO
    // =========================================================================
    @Operation(summary = "Criar um novo documento de projeto")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Documento de projeto criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    @PostMapping("/documento")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> criarDocumentoProjeto(@Valid @RequestBody DocumentoProjetoRequest request) {
        DocumentoProjetoResponse response = serviceDocumento.criarDocumentoProjeto(request);
        return ResponseHttpBuilder.created("Documento do projeto criado com sucesso.", response);
    }

    // =========================================================================
    // UPDATE DOCUMENTO DO PROJECTO
    // =========================================================================
    @Operation(summary = "Atualizar documento do projeto")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Documento do projeto atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Documento do projeto não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    @PutMapping("/documento/{id}")
    public ResponseEntity<?> atualizarDocumentoProjeto(@PathVariable UUID id, @Valid @RequestBody DocumentoProjetoRequest request) {
        DocumentoProjetoResponse response = serviceDocumento.atualizar(id, request);
        return ResponseHttpBuilder.info("Documento do projeto atualizado com sucesso.", response);
    }

    // =========================================================================
    // DELETE DOCUMENTO DO PROJECTO
    // =========================================================================
    @Operation(summary = "Eliminar documento de projeto")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Documento de projeto eliminado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Documento de projeto não encontrado")
    })
    @DeleteMapping("/documento/{id}")
    public ResponseEntity<?> eliminarDocumentoProjeto(@PathVariable UUID id) {
        serviceDocumento.excluir(id);
        return ResponseEntity.noContent().build();
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

    // =========================================================================
    // READ - BY ID DOCUMENTO DO PROJECTO
    // =========================================================================     
    @Operation(summary = "Buscar documento do projeto por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Documento de projeto encontrada"),
            @ApiResponse(responseCode = "404", description = "Documento de projeto não encontrada")
    })
    @GetMapping("/documento/buscar/{id}")
    public ResponseEntity<?> pesguisarDocumentoProjetoById(@PathVariable("id") UUID id)
    {
        DocumentoProjetoResponse response = serviceDocumento.buscarPorId(id);
        return ResponseHttpBuilder.info("Documento recuperado com sucesso.", response);
    }

    // =========================================================================
    // READ - ID DOCUMENTO DO PROJECTO
    // =========================================================================   
    @Operation(summary = "Listar documentos do projeto (paginado)")
    @ApiResponse(responseCode = "200", description = "Lista encontrada")
    @GetMapping("/documento/{id}")
    public ResponseEntity<?> listaDeDocumentoProjetos(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "12", required = false) int size,
            @PathVariable("id") Integer id
    ){
        Pageable pageable = PageRequest.of(page, size);
        Page<DocumentoProjetoResponse> result = serviceDocumento.listar(pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("content", result.getContent());
        response.put("page", result.getNumber());
        response.put("size", result.getSize());
        response.put("totalElements", result.getTotalElements());
        response.put("totalPages", result.getTotalPages());

        return ResponseHttpBuilder.info("Lista de documentos recuperada com sucesso.", response);
    }

}
