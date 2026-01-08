package ao.prumo.obra.obramanagementservice.controller;

import ao.prumo.obra.obramanagementservice.entity.dto.request.FuncionarioRequest;
import ao.prumo.obra.obramanagementservice.entity.dto.response.FuncionarioResponse;
import ao.prumo.obra.obramanagementservice.service.FuncionarioService;
import ao.prumo.obra.obramanagementservice.entity.dto.request.CargoRequest;
import ao.prumo.obra.obramanagementservice.entity.dto.response.CargoResponse;
import ao.prumo.obra.obramanagementservice.service.CargoService;
import ao.prumo.obra.obramanagementservice.utils.globalConstantes.Constante;
import ao.prumo.obra.obramanagementservice.utils.http.ResponseHttp;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(Constante.ROUTE + "/funcionario")
@Tag(name="Funcionario", description="Gestão de obras/projetos, funcionario associados")
@RequiredArgsConstructor
public class FuncionarioController
{
     private final FuncionarioService service;
     private final CargoService serviceCargo;

    @Operation(summary = "Criar um novo funcionario", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @io.swagger.v3.oas.annotations.media.Content(encoding = @io.swagger.v3.oas.annotations.media.Encoding(name = "request", contentType = "application/json"))))
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Funcionario criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    @PostMapping(value="/criar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseHttp> criarFuncionario(@Valid @RequestPart("request") FuncionarioRequest request, @RequestPart("file") MultipartFile file ,
                                                         @AuthenticationPrincipal Jwt jwt)
    {
        FuncionarioResponse response = service.criarFuncionario(request, file, jwt);
        return ResponseHttpBuilder.created("Funcionário criado com sucesso.", response);
    }

    @Operation(summary = "Listar funcionários (paginado)")
    @ApiResponse(responseCode = "200", description = "Lista de funcionarios encontrados")
    @GetMapping("/pages")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<?> listaDeFuncionarios( 
        @RequestParam(name = "page", defaultValue = "0", required = false) int page,
        @RequestParam(name = "size", defaultValue = "12", required = false) int size,
        @AuthenticationPrincipal Jwt jwt
    ){
        Pageable pageable = PageRequest.of(page, size);
        Page<FuncionarioResponse> result = service.listar(pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("content", result.getContent());
        response.put("page", result.getNumber());
        response.put("size", result.getSize());
        response.put("totalElements", result.getTotalElements());
        response.put("totalPages", result.getTotalPages());

        return ResponseHttpBuilder.info("Lista de funcionários recuperada.", response);
    }

    // =========================================================================
    // READ - BY ID
    // =========================================================================
    @Operation(summary = "Buscar funcionário por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Funcionario encontrado"),
            @ApiResponse(responseCode = "404", description = "Funcionario não encontrada")
    })
    @GetMapping("/buscar/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<?> pesguisarFuncionarioById(@PathVariable("id") UUID id, @AuthenticationPrincipal Jwt jwt)
    {
        FuncionarioResponse response = service.buscarPorId(id);
        return ResponseHttpBuilder.info("Cliente recuperado com sucesso.", response);
    }

    @Operation(summary = "Atualizar funcionario existente", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @io.swagger.v3.oas.annotations.media.Content(encoding = @io.swagger.v3.oas.annotations.media.Encoding(name = "request", contentType = "application/json"))))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Funcionario atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Funcionario não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    @PutMapping(value="/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<?> atualizarFuncionario(@PathVariable UUID id, @Valid @RequestPart("request") FuncionarioRequest request, @RequestPart("file") MultipartFile file, @AuthenticationPrincipal Jwt jwt) 
    {
        FuncionarioResponse response = service.alterarFuncionario(id, request, file);
        return ResponseHttpBuilder.info("Funcionario atualizado com sucesso.", response);
    }

    @Operation(summary = "Eliminar funcionario")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Funcionario eliminado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Funcionario não encontrado")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<?> excluirFuncionario(@PathVariable UUID id, @AuthenticationPrincipal Jwt jwt) {
        service.excluirFuncionario(id);
        return ResponseEntity.noContent().build();
    }

    // =========================================================================
    // CREATE FUNCIONARIO
    // =========================================================================
    @Operation(summary = "Criar um novo cargo")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cargo criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    @PostMapping("/cargo")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<?> criarCargo(@Valid @RequestBody CargoRequest request, @AuthenticationPrincipal Jwt jwt) {
        CargoResponse response = serviceCargo.criarCargo(request, jwt);
        return ResponseHttpBuilder.created("Cargo criado com sucesso.", response);
    }

    // =========================================================================
    // READ - LIST (PAGINADO)  FUNCIONARIO
    // =========================================================================
    @Operation(summary = "Listar cargos (paginado)")
    @ApiResponse(responseCode = "200", description = "Lista de cargos encontrada")
    @GetMapping("/cargo/pages")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<?> listarCargos(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "12", required = false) int size,
            @AuthenticationPrincipal Jwt jwt
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CargoResponse> cargos = serviceCargo.listarCargos(pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("content", cargos.getContent());
        response.put("page", cargos.getNumber());
        response.put("size", cargos.getSize());
        response.put("totalElements", cargos.getTotalElements());
        response.put("totalPages", cargos.getTotalPages());

        return ResponseHttpBuilder.info("Lista de cargos recuperada com sucesso.", response);
    }

    // =========================================================================
    // READ - BY ID  Cargo
    // =========================================================================
    @Operation(summary = "Buscar cargo por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cargo encontrado"),
            @ApiResponse(responseCode = "404", description = "Cargo não encontrado")
    })
    @GetMapping("/cargo/buscar/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<?> buscarCargoPorId(@PathVariable UUID id, @AuthenticationPrincipal Jwt jwt) {
        CargoResponse response = serviceCargo.buscarCargoPorId(id);
        return ResponseHttpBuilder.info("Cargo recuperado com sucesso.", response);
    }

    // =========================================================================
    // UPDATE  Cargo
    // =========================================================================
    @Operation(summary = "Atualizar um cargo existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cargo atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cargo não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    @PutMapping("/cargo/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<?> atualizarCargo(@PathVariable UUID id, @Valid @RequestBody CargoRequest request, @AuthenticationPrincipal Jwt jwt) 
    {
        CargoResponse response = serviceCargo.alterarCargo(id, request);
        return ResponseHttpBuilder.info("Cargo atualizado com sucesso.", response);
    }

    // =========================================================================
    // DELETE  Cargo
    // =========================================================================
    @Operation(summary = "Eliminar cargo")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Cargo eliminado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cargo não encontrado")
    })
    @DeleteMapping("/cargo/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<?> excluirCargo(@PathVariable UUID id, @AuthenticationPrincipal Jwt jwt) {
        serviceCargo.excluirCargo(id);
        return ResponseEntity.noContent().build();
    }
}
