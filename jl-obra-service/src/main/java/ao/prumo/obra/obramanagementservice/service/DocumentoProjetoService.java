package ao.prumo.obra.obramanagementservice.service;

import ao.prumo.obra.obramanagementservice.entity.DocumentoProjeto;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public DocumentoProjetoResponse criarDocumentoProjeto(DocumentoProjetoRequest request)
    {
        log.info("Iniciando a criação de um novo documento do projeto");
        DocumentoProjeto documento = mapper.toEntity(request);
        return mapper.toResponse(repository.save(documento));
    }

    // =========================================================================
    // READ - LIST (PAGINADO)
    // =========================================================================
    @Transactional(readOnly = true)
    public Page<DocumentoProjetoResponse> listar(Pageable pageable)
    {
        log.info("Iniciando a listagem de documentos do projeto.");
        return repository.findAll(pageable)
                .map(mapper::toResponse);
    }

    // =========================================================================
    // READ - BY ID
    // =========================================================================
    @Transactional(readOnly = true)
    public DocumentoProjetoResponse buscarPorId(UUID id)
    {
        log.info("Iniciando a busca de documento do projeto por ID {}.", id);
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

        DocumentoProjeto atualizado = mapper.toEntity(request);
        atualizado.setId(existente.getId());
        log.info("Documento do Projeto com ID {} alterada com sucesso.", id);
        return mapper.toResponse(repository.save(atualizado));
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

        repository.delete(documento);
        log.info("Documento do Projeto com ID {} removido com sucesso", id);
    }

}