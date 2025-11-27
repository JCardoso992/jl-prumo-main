package ao.prumo.obra.obramanagementservice.controller;

import ao.prumo.obra.obramanagementservice.entity.ContaOrganizacao;
import ao.prumo.obra.obramanagementservice.entity.dto.request.ContaOrganizacaoRequest;
import ao.prumo.obra.obramanagementservice.entity.dto.response.ContaOrganizacaoResponse;
import ao.prumo.obra.obramanagementservice.service.ContaOrganizacaoService;
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
@RequestMapping(Constante.ROUTE + "/contaorganizacao")
@Tag(name="Conta da Organização", description="Gestão de obras/projetos, conta da organização associadas")
@RequiredArgsConstructor
public class ContaOrganizacaoController
{
    @Autowired
    private ContaOrganizacaoService service;

    @Operation(summary = "Lista de contas da organização paginadas")
    @ApiResponse(responseCode = "200", description = "Lista de contas da organização encontradas páginadas")
    @GetMapping("/pages/{id}")
    public ResponseEntity<HashMap> listaDeContaOrganizacaos(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "12", required = false) int size,
            @PathVariable("id") Integer id
    ){
        Pageable pageable = PageRequest.of(page, size);
        Page<ContaOrganizacao> ContaOrganizacaoPage = service.findAll(pageable);
        ContaOrganizacaoResponse response = new ContaOrganizacaoResponse();
        return ResponseEntity.ok(response.paginar(ContaOrganizacaoPage));
    }

    @Operation(summary = "Lista de contas da organização")
    @ApiResponse(responseCode = "200", description = "Lista de contas da organização encontradas")
    @GetMapping("/{id}")
    public ResponseEntity<?> listaDeContaOrganizacaos(@PathVariable("id") UUID id)
    {
        List<ContaOrganizacao> ContaOrganizacaoPage = service.findAll();
        ContaOrganizacaoResponse response = new ContaOrganizacaoResponse();
        return ResponseEntity.ok(response.listToDTO(ContaOrganizacaoPage));
    }

    @Operation(summary = "Pesquisar determinada conta da organização")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Conta da organização encontrada"),
            @ApiResponse(responseCode = "404", description = "Conta da organização não encontrada")
    })
    @GetMapping("/pesquisar/{id}")
    public ResponseEntity<?> pesguisarContaOrganizacaoById(@PathVariable("id") UUID id)
    {
        ContaOrganizacao ContaOrganizacao = service.findById(id);
        ContaOrganizacaoResponse response = new ContaOrganizacaoResponse();
        return ResponseEntity.ok(response.convertToDTO(ContaOrganizacao));
    }

    @Operation(summary = "Criar uma nova conta para a organização")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Conta da organização criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ContaOrganizacaoResponse> criarContaOrganizacao(@Valid @RequestBody ContaOrganizacaoRequest request) {
        ContaOrganizacao novaContaOrganizacao = service.save(request.convertToEntity());
        ContaOrganizacaoResponse response = new ContaOrganizacaoResponse().convertToDTO(novaContaOrganizacao);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Atualizar conta da organização existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Conta da organização atualizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Conta da organização não encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ContaOrganizacaoResponse> atualizarContaOrganizacao(@PathVariable UUID id, @Valid @RequestBody ContaOrganizacaoRequest request) {
        ContaOrganizacao ContaOrganizacaoAtualizada = service.update(id, request.convertToEntity());
        ContaOrganizacaoResponse response = new ContaOrganizacaoResponse().convertToDTO(ContaOrganizacaoAtualizada);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Eliminar conta da organização existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Conta da organização eliminada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Conta da organização não encontrada")
    })
    @PutMapping("/eliminar/{id}")
    public ResponseEntity<ContaOrganizacaoResponse> eliminarContaOrganizacao(@PathVariable UUID id, @Valid @RequestBody ContaOrganizacaoRequest request) {
        ContaOrganizacao ContaOrganizacaoAtualizada = service.update(id, request.convertToEntity());
        ContaOrganizacaoResponse response = new ContaOrganizacaoResponse().convertToDTO(ContaOrganizacaoAtualizada);
        return ResponseEntity.ok(response);
    }
}
