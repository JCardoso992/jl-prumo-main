package ao.prumo.obra.obramanagementservice.controller;

import ao.prumo.obra.obramanagementservice.entity.dto.request.OrganizacaoRequest;
import ao.prumo.obra.obramanagementservice.entity.dto.response.OrganizacaoResponse;
import ao.prumo.obra.obramanagementservice.service.OrganizacaoService;
import ao.prumo.obra.obramanagementservice.entity.dto.request.ContaOrganizacaoRequest;
import ao.prumo.obra.obramanagementservice.entity.dto.response.ContaOrganizacaoResponse;
import ao.prumo.obra.obramanagementservice.service.ContaOrganizacaoService;
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
@RequestMapping(Constante.ROUTE + "/organizacao")
@Tag(name="Organização", description="Gestão de organizações")
@RequiredArgsConstructor
public class OrganizacaoController
{
    private final OrganizacaoService service;

    private final ContaOrganizacaoService serviceConta;

    // =========================================================================
    // CREATE
     // 1.1. Swagger UI (OpenAPI)
    // @Operation(summary = "Criar uma nova agência", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @io.swagger.v3.oas.annotations.media.Content(encoding = @io.swagger.v3.oas.annotations.media.Encoding(name = "request", contentType = "application/json"))))
    //  @PostMapping(value="/criar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    //  1.2. Postman
    //  @PostMapping(value="/criar", consumes = "multipart/form-data")
    // =========================================================================
    @Operation(summary = "Criar uma nova organização", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @io.swagger.v3.oas.annotations.media.Content(encoding = @io.swagger.v3.oas.annotations.media.Encoding(name = "request", contentType = "application/json"))))
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Organização criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    @PostMapping(value="/criar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> criar(@Valid @RequestPart("request")  OrganizacaoRequest request,
        @RequestPart("file") MultipartFile file )
    {
        OrganizacaoResponse response = service.criar(request, file);
        return ResponseHttpBuilder.created("Organização criada com sucesso.", response);
    }

    // =========================================================================
    // READ - LIST
    // =========================================================================
    @Operation(summary = "Listar organizações (paginado)")
    @ApiResponse(responseCode = "200", description = "Lista de organização encontrado")
    @GetMapping("/pages")
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
    @GetMapping("/buscar/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable UUID id) 
    {
        OrganizacaoResponse response = service.buscarPorId(id);
        return ResponseHttpBuilder.info("Organização recuperada com sucesso.", response);
    }

    // =========================================================================
    // UPDATE
    // =========================================================================
    @Operation(summary = "Atualizar organização", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @io.swagger.v3.oas.annotations.media.Content(encoding = @io.swagger.v3.oas.annotations.media.Encoding(name = "request", contentType = "application/json"))))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Organização atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Organização não encontrada"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    @PutMapping(value="/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> atualizar(
        @PathVariable UUID id,
        @Valid @RequestBody OrganizacaoRequest request, @RequestPart("file") MultipartFile file )
    {
        OrganizacaoResponse response = service.atualizar(id, request, file);
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

    // =========================================================================
    // CREATE CONTA
    // =========================================================================
    @Operation(summary = "Criar uma nova conta da organização")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Conta criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    @PostMapping("/conta")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> criarConta(@Valid @RequestBody ContaOrganizacaoRequest request) {
        ContaOrganizacaoResponse response = serviceConta.criarContaOrganizacao(request);
        return ResponseHttpBuilder.created("Conta da organização criada com sucesso.", response);
    }

    // =========================================================================
    // READ - LIST (PAGINADO) CONTA
    // =========================================================================
    @Operation(summary = "Listar contas da organização (paginado)")
    @ApiResponse(responseCode = "200", description = "Lista encontrada")
    @GetMapping("/conta/pages/{id}")
    public ResponseEntity<?> listarConta(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "12", required = false) int size,
            @PathVariable("id") UUID id
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ContaOrganizacaoResponse> result = serviceConta.listar(pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("content", result.getContent());
        response.put("page", result.getNumber());
        response.put("size", result.getSize());
        response.put("totalElements", result.getTotalElements());
        response.put("totalPages", result.getTotalPages());

        return ResponseHttpBuilder.info("Lista de contas recuperada com sucesso.", response);
    }

    // =========================================================================
    // READ - BY ID CONTA
    // =========================================================================
    @Operation(summary = "Buscar conta da organização por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Conta encontrada"),
            @ApiResponse(responseCode = "404", description = "Conta não encontrada")
    })
    @GetMapping("/conta/buscar/{id}")
    public ResponseEntity<?> buscarContaPorId(@PathVariable UUID id) {
        ContaOrganizacaoResponse response = serviceConta.buscarPorId(id);
        return ResponseHttpBuilder.info("Conta recuperada com sucesso.", response);
    }

    // =========================================================================
    // UPDATE CONTA
    // =========================================================================
    @Operation(summary = "Atualizar conta da organização")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Conta atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Conta não encontrada"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    @PutMapping("/conta/{id}")
    public ResponseEntity<?> atualizar(
            @PathVariable UUID id,
            @Valid @RequestBody ContaOrganizacaoRequest request) {
        ContaOrganizacaoResponse response = serviceConta.atualizar(id, request);
        return ResponseHttpBuilder.info("Conta atualizada com sucesso.", response);
    }

    // =========================================================================
    // DELETE CONTA
    // =========================================================================
    @Operation(summary = "Eliminar conta da organização")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Conta  eliminada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Conta não encontrada")
    })
    @DeleteMapping("/conta/{id}")
    public ResponseEntity<?> excluirConta(@PathVariable UUID id) {
        serviceConta.excluir(id);
        return ResponseEntity.noContent().build();
    }

}