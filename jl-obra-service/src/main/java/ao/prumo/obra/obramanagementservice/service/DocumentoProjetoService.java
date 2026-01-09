package ao.prumo.obra.obramanagementservice.service;

import ao.prumo.obra.obramanagementservice.entity.DocumentoProjeto;
import ao.prumo.obra.obramanagementservice.entity.Funcionario;
import ao.prumo.obra.obramanagementservice.entity.VersaoProjeto;
import ao.prumo.obra.obramanagementservice.entity.Organizacao;
import ao.prumo.obra.obramanagementservice.entity.dto.mapper.DocumentoProjetoMapper;
import ao.prumo.obra.obramanagementservice.entity.dto.request.DocumentoProjetoRequest;
import ao.prumo.obra.obramanagementservice.entity.dto.response.DocumentoProjetoResponse;
import ao.prumo.obra.obramanagementservice.entity.repository.DocumentoProjetoRepository;
import ao.prumo.obra.obramanagementservice.utils.exception.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DocumentoProjetoService
{

    private final DocumentoProjetoRepository repository;
    private final DocumentoProjetoMapper mapper;

    protected JpaRepository<DocumentoProjeto, UUID> getRepository()
    {
        return this.repository;
    }

     // =========================================================================
    // CREATE
    // =========================================================================
    @Transactional
    public DocumentoProjetoResponse criarDocumentoProjeto(DocumentoProjetoRequest request, Jwt jwt)
    {
        log.info("Iniciando a criação de um novo documento do projeto");
        // 1. Extrair o ID da organização do TOKEN
        Map<String, Object> appMetadata = jwt.getClaimAsMap("app_metadata");
        String orgIdToken = (String) appMetadata.get("org_id");

        if (orgIdToken == null) {
            log.error("Claims presentes no token: {}", jwt.getClaims()); // Isso ajuda a debugar no log
            throw new AccessDeniedException("Utilizador não vinculado a uma organização.");
        }
        UUID organizacaoId = UUID.fromString(orgIdToken);
        DocumentoProjeto documento = mapper.toEntity(request);
        documento.setArquitectoDesenhoId(new Funcionario(request.getCodArquitectoDesenho()));
        documento.setOrganizacaoId(new Organizacao(organizacaoId));
        documento.setVersaoProjetoId(new VersaoProjeto(request.getCodVersaoProjeto()));
        documento.setStatus(Boolean.TRUE);
        DocumentoProjeto documentoCriado = repository.save(documento);
        log.info("Documento do Projeto criado com sucesso.");
        return mapper.toResponse(documentoCriado);
    }

    // =========================================================================
    // READ - LIST (PAGINADO)
    // =========================================================================
    @Transactional(readOnly = true)
    public Page<DocumentoProjetoResponse> listar(Pageable pageable, Jwt jwt)
    {
        log.info("Iniciando a listagem de documentos do projeto.");
        // 1. Extrair o ID da organização do TOKEN
        Map<String, Object> appMetadata = jwt.getClaimAsMap("app_metadata");
        String orgIdToken = (String) appMetadata.get("org_id");

        if (orgIdToken == null) {
            log.error("Claims presentes no token: {}", jwt.getClaims()); // Isso ajuda a debugar no log
            throw new AccessDeniedException("Utilizador não vinculado a uma organização.");
        }
        return repository.findAll(pageable)
                .map(mapper::toResponse);
    }

    // =========================================================================
    // READ - BY ID
    // =========================================================================
    @Transactional(readOnly = true)
    public DocumentoProjetoResponse buscarPorId(UUID id, Jwt jwt)
    {
        log.info("Iniciando a busca de documento do projeto por ID {}.", id);
        // 1. Extrair o ID da organização do TOKEN
        Map<String, Object> appMetadata = jwt.getClaimAsMap("app_metadata");
        String orgIdToken = (String) appMetadata.get("org_id");

        if (orgIdToken == null) {
            log.error("Claims presentes no token: {}", jwt.getClaims()); // Isso ajuda a debugar no log
            throw new AccessDeniedException("Utilizador não vinculado a uma organização.");
        }
        DocumentoProjeto documento = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Documento do projeto não encontrado"));
        log.info("Documento do Projeto com ID {} foi encontrada.", id);        
        return mapper.toResponse(documento);
    }

    // =========================================================================
    // UPDATE
    // =========================================================================
    /**
    * Atualiza documento do projeto existente buscando pelo ID.
    * @param id O UUID do documento do projeto a ser alterada.
    * @param request O DTO com os novos dados.
    * @return DocumentoProjetoResponse com os dados atualizados.
    * @throws ResourceNotFoundException se o documento do projeto não for encontrada.
    */
    @Transactional
    public DocumentoProjetoResponse atualizar(UUID id, DocumentoProjetoRequest request) 
    {
        log.info("Iniciando a atualização do documento do projeto com ID {}.", id);
        DocumentoProjeto existente = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Documento do projeto não encontrado"));

        mapper.updateEntityFromDto(request, existente);
        DocumentoProjeto existenteAtualizado = repository.save(existente);
        log.info("Documento do Projeto com ID {} alterada com sucesso.", id);
        return mapper.toResponse(existenteAtualizado);
    }

    // =========================================================================
    // DELETE
    // =========================================================================
    /**
    * Elimina documento do projeto pelo seu ID.
    * @param id O UUID do documento do projeto a ser excluída.
    * @throws ResourceNotFoundException se o documento do projeto não for encontrada.
    */
    @Transactional
    public void excluir(UUID id) 
    {
        log.info("Iniciando a exclusão do documento do projeto com ID {}.", id);
        DocumentoProjeto documento = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Documento do projeto não encontrado"));
        documento.setStatus(Boolean.TRUE);
        repository.save(documento);
        log.info("Documento do Projeto com ID {} removido com sucesso", id);
    }

}