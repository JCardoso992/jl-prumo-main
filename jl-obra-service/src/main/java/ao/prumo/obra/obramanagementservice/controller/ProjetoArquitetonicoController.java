package ao.prumo.obra.obramanagementservice.controller;

import ao.prumo.obra.obramanagementservice.entity.ProjetoArquitetonico;
import ao.prumo.obra.obramanagementservice.entity.dto.request.ProjetoArquitetonicoRequest;
import ao.prumo.obra.obramanagementservice.entity.dto.response.ProjetoArquitetonicoResponse;
import ao.prumo.obra.obramanagementservice.service.ProjetoArquitetonicoService;
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
@RequestMapping(Constante.ROUTE + "/projeto")
@Tag(name="Projeto Arquitetonico", description="Gestão de obras/projetos, projetos arquitetonico associados")
@RequiredArgsConstructor
public class ProjetoArquitetonicoController
{
    @Autowired
    private ProjetoArquitetonicoService service;

    @Operation(summary = "Lista de projetos arquitetonicos paginadas")
    @ApiResponse(responseCode = "200", description = "Lista de projetos arquitetonicos encontrados páginados")
    @GetMapping("/pages/{id}")
    public ResponseEntity<HashMap> listaDeProjetoArquitetonicos(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "12", required = false) int size,
            @PathVariable("id") Integer id
    ){
        Pageable pageable = PageRequest.of(page, size);
        Page<ProjetoArquitetonico> projetoArquitetonicoPage = service.findAll(pageable);
        ProjetoArquitetonicoResponse response = new ProjetoArquitetonicoResponse();
        return ResponseEntity.ok(response.paginar(projetoArquitetonicoPage));
    }

    @Operation(summary = "Lista de projetos arquitetonicos")
    @ApiResponse(responseCode = "200", description = "Lista de projetos arquitetonicos encontrados")
    @GetMapping("/{id}")
    public ResponseEntity<?> listaDeProjetoArquitetonicos(@PathVariable("id") UUID id)
    {
        List<ProjetoArquitetonico> projetoArquitetonicoPage = service.findAll();
        ProjetoArquitetonicoResponse response = new ProjetoArquitetonicoResponse();
        return ResponseEntity.ok(response.listToDTO(projetoArquitetonicoPage));
    }

    @Operation(summary = "Pesquisar determinada projeto arquitetonico")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Projeto arquitetonico encontrado"),
            @ApiResponse(responseCode = "404", description = "Projeto arquitetonico não encontrado")
    })
    @GetMapping("/pesquisar/{id}")
    public ResponseEntity<?> pesguisarProjetoArquitetonicoById(@PathVariable("id") UUID id)
    {
        ProjetoArquitetonico projetoArquitetonico = service.findById(id);
        ProjetoArquitetonicoResponse response = new ProjetoArquitetonicoResponse();
        return ResponseEntity.ok(response.convertToDTO(projetoArquitetonico));
    }

    @Operation(summary = "Criar uma novo projeto arquitetonico")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Projeto arquitetonico criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ProjetoArquitetonicoResponse> criarProjetoArquitetonico(@Valid @RequestBody ProjetoArquitetonicoRequest request) {
        ProjetoArquitetonico novaProjetoArquitetonico = service.save(request.convertToEntity());
        ProjetoArquitetonicoResponse response = new ProjetoArquitetonicoResponse().convertToDTO(novaProjetoArquitetonico);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Atualizar Projeto arquitetonico existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Projeto arquitetonico atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Projeto arquitetonico não encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProjetoArquitetonicoResponse> atualizarProjetoArquitetonico(@PathVariable UUID id, @Valid @RequestBody ProjetoArquitetonicoRequest request) {
        ProjetoArquitetonico projetoArquitetonicoAtualizada = service.update(id, request.convertToEntity());
        ProjetoArquitetonicoResponse response = new ProjetoArquitetonicoResponse().convertToDTO(projetoArquitetonicoAtualizada);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Eliminar Projeto arquitetonico existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Projeto arquitetonico eliminado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Projeto arquitetonico não encontrado")
    })
    @PutMapping("/eliminar/{id}")
    public ResponseEntity<ProjetoArquitetonicoResponse> eliminarprojetoArquitetonico(@PathVariable UUID id, @Valid @RequestBody ProjetoArquitetonicoRequest request) {
        ProjetoArquitetonico projetoArquitetonicoAtualizada = service.update(id, request.convertToEntity());
        ProjetoArquitetonicoResponse response = new ProjetoArquitetonicoResponse().convertToDTO(projetoArquitetonicoAtualizada);
        return ResponseEntity.ok(response);
    }
}
