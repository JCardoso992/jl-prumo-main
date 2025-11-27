package ao.prumo.obra.obramanagementservice.controller;

import ao.prumo.obra.obramanagementservice.entity.Funcionario;
import ao.prumo.obra.obramanagementservice.entity.dto.request.FuncionarioRequest;
import ao.prumo.obra.obramanagementservice.entity.dto.response.FuncionarioResponse;
import ao.prumo.obra.obramanagementservice.service.FuncionarioService;
import ao.prumo.obra.obramanagementservice.utils.globalConstantes.Constante;
import ao.prumo.obra.obramanagementservice.utils.http.ResponseHttp;
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
@RequestMapping(Constante.ROUTE + "/funcionario")
@Tag(name="Funcionario", description="Gestão de obras/projetos, funcionario associados")
@RequiredArgsConstructor
public class FuncionarioController
{
    @Autowired
    private FuncionarioService service;

    @Operation(summary = "Lista de funcionarios paginados")
    @ApiResponse(responseCode = "200", description = "Lista de funcionarios encontrados páginados")
    @GetMapping("/pages/{id}")
    public ResponseEntity<HashMap> listaDeFuncionarios(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "12", required = false) int size,
            @PathVariable("id") Integer id
    ){
        Pageable pageable = PageRequest.of(page, size);
        Page<Funcionario> funcionarioPage = service.findAll(pageable);
        FuncionarioResponse response = new FuncionarioResponse();
        return ResponseEntity.ok(response.paginar(funcionarioPage));
    }

    @Operation(summary = "Lista de funcionarios")
    @ApiResponse(responseCode = "200", description = "Lista de funcionarios encontrados")
    @GetMapping("/{id}")
    public ResponseEntity<?> listaDeFuncionarios(@PathVariable("id") UUID id)
    {
        List<Funcionario> funcionarioPage = service.findAll();
        FuncionarioResponse response = new FuncionarioResponse();
        return ResponseEntity.ok(response.listToDTO(funcionarioPage));
    }

    @Operation(summary = "Pesquisar determinado funcionario")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Funcionario encontrado"),
            @ApiResponse(responseCode = "404", description = "Funcionario não encontrada")
    })
    @GetMapping("/pesquisar/{id}")
    public ResponseEntity<?> pesguisarFuncionarioById(@PathVariable("id") UUID id)
    {
        Funcionario funcionario = service.findById(id);
        FuncionarioResponse response = new FuncionarioResponse();
        return ResponseEntity.ok(response.convertToDTO(funcionario));
    }

    @Operation(summary = "Criar um novo registro")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Registro criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    @PostMapping
    public ResponseEntity<ResponseHttp> criarFuncionario(@Valid @RequestBody FuncionarioRequest request) {
        FuncionarioResponse response = service.criarFuncionarioCompleto(request);
        return ResponseHttpBuilder.created("Registro criado com sucesso!", response);
    }

  /*  @Operation(summary = "Atualizar funcionario existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Funcionario atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Funcionario não encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<FuncionarioResponse> atualizarFuncionario(@PathVariable UUID id, @Valid @RequestBody FuncionarioRequest request) {
        Funcionario funcionarioAtualizada = service.update(id, request.convertToEntity());
        FuncionarioResponse response = new FuncionarioResponse().convertToDTO(funcionarioAtualizada);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Eliminar funcionario existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Funcionario eliminado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Funcionario não encontrado")
    })
    @PutMapping("/eliminar/{id}")
    public ResponseEntity<FuncionarioResponse> eliminarFuncionario(@PathVariable UUID id, @Valid @RequestBody FuncionarioRequest request) {
        Funcionario funcionarioAtualizada = service.update(id, request.convertToEntity());
        FuncionarioResponse response = new FuncionarioResponse().convertToDTO(funcionarioAtualizada);
        return ResponseEntity.ok(response);
    }*/
}
