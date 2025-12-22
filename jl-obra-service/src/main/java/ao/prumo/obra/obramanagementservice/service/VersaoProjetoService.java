package ao.prumo.obra.obramanagementservice.service;


import ao.prumo.obra.obramanagementservice.entity.VersaoProjeto;
import ao.prumo.obra.obramanagementservice.entity.dto.mapper.VersaoProjetoMapper;
import ao.prumo.obra.obramanagementservice.entity.dto.request.VersaoProjetoRequest;
import ao.prumo.obra.obramanagementservice.entity.dto.response.VersaoProjetoResponse;
import ao.prumo.obra.obramanagementservice.entity.repository.VersaoProjetoRepository;
import ao.prumo.obra.obramanagementservice.utils.base.BaseService;
import ao.prumo.obra.obramanagementservice.utils.exception.ResourceNotFoundException;
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
public class VersaoProjetoService extends BaseService<VersaoProjeto, UUID> {

    private final VersaoProjetoRepository repository;
    private final VersaoProjetoMapper mapper;

    protected JpaRepository<VersaoProjeto, UUID> getRepository()
    {
        return this.repository;
    }

    // =========================================================================
    // CREATE
    // =========================================================================
    @Transactional
    public VersaoProjetoResponse criar(VersaoProjetoRequest request)
    {
        log.info("Iniciando a criação de nova versão de projeto");
        VersaoProjeto entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }

    // =========================================================================
    // READ - LIST
    // =========================================================================
    @Transactional(readOnly = true)
    public Page<VersaoProjetoResponse> listar(Pageable pageable)
    {
        log.info("Iniciando a listagem de versões de projetos.");
        return repository.findAll(pageable)
                .map(mapper::toResponse);
    }

    // =========================================================================
    // READ - BY ID
    // =========================================================================
    @Transactional(readOnly = true)
    public VersaoProjetoResponse buscarPorId(UUID id) 
    {
        log.info("Iniciando a busca de versão de projeto por ID.");
        VersaoProjeto versao = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Versão de projeto não encontrada"));
        log.info("Versão de projeto com ID {} foi encontrada.", id);        
        return mapper.toResponse(versao);
    }

    // =========================================================================
    // UPDATE
    // =========================================================================
    /**
    * Atualiza da versão do projeto existente buscando pelo ID.
    * @param id O UUID da versão de projeto a ser alterada.
    * @param request O DTO com os novos dados.
    * @return VersaoProjetoResponse com os dados atualizados.
    * @throws ResourceNotFoundException se a versão de projeto não for encontrada.
    */
   @Transactional
    public VersaoProjetoResponse atualizar(UUID id, VersaoProjetoRequest request) 
    {
        log.info("Iniciando a atualização da versão de projeto com ID {}.", id);
        VersaoProjeto existente = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Versão de projeto não encontrada"));

        VersaoProjeto atualizado = mapper.toEntity(request);
        atualizado.setId(existente.getId());
        VersaoProjeto atualizadoSalvo = repository.save(atualizado);
        log.info("Versão de projeto com ID {} alterada com sucesso.", id);
        return mapper.toResponse(atualizadoSalvo);
    }

    // =========================================================================
    // DELETE
    // =========================================================================
    /**
    * Elimina uma versão do projeto pelo seu ID.
    * @param id O UUID da versão do projeto a ser excluída.
    * @throws ResourceNotFoundException se a versão do projeto não for encontrada.
    */
    @Transactional
    public void excluir(UUID id) 
    {
        log.info("Iniciando a exclusão da versão de projeto com ID {}.", id);
        VersaoProjeto versao = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Versão de projeto não encontrada"));
        repository.delete(versao);

        log.info("Versão de projeto com ID {} removida com sucesso", id);
    }
}