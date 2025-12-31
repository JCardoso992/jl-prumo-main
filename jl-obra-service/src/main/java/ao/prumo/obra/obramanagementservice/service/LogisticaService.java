package ao.prumo.obra.obramanagementservice.service;

import ao.prumo.obra.obramanagementservice.entity.Despesa;
import ao.prumo.obra.obramanagementservice.entity.Logistica;
import ao.prumo.obra.obramanagementservice.entity.Organizacao;
import ao.prumo.obra.obramanagementservice.entity.dto.mapper.LogisticaMapper;
import ao.prumo.obra.obramanagementservice.entity.dto.request.LogisticaRequest;
import ao.prumo.obra.obramanagementservice.entity.dto.response.LogisticaResponse;
import ao.prumo.obra.obramanagementservice.entity.repository.LogisticaRepository;
import ao.prumo.obra.obramanagementservice.utils.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
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
public class LogisticaService
{

    private final LogisticaRepository repository;
    private final LogisticaMapper mapper;

    protected JpaRepository<Logistica, UUID> getRepository()
    {
        return this.repository;
    }

    // =========================================================================
    // CREATE
    // =========================================================================
    @Transactional
    @CacheEvict(value = "buscar-equipamentos", allEntries = true)
    public LogisticaResponse criar(LogisticaRequest request) {
        log.info("Iniciando a criação de um novo item de logística");
        Logistica entity = mapper.toEntity(request);
        // idOrganização= bb1827b3-57b9-49ec-a775-a7f5a91b8297
        entity.setOrganizacaoId(new Organizacao(UUID.fromString("bb1827b3-57b9-49ec-a775-a7f5a91b8297")));
        entity.setMercadoriaId(new Despesa(request.getCodMercadoria()));
        Logistica entitySalva = repository.save(entity);
        log.info("Item de logística criado com sucesso.");
        return mapper.toResponse(entitySalva);
    }

    // =========================================================================
    // READ - LIST (PAGINADO)
    // =========================================================================
    @Transactional(readOnly = true)
    @Cacheable("buscar-equipamentos")
    public Page<LogisticaResponse> listar(Pageable pageable)
    {
        log.info("Iniciando a listagem de item de logística.");
        return repository.findAll(pageable)
                .map(mapper::toResponse);
    }

    // =========================================================================
    // READ - BY ID
    // =========================================================================
    @Transactional(readOnly = true)
    @Cacheable("buscar-equipamento-por-id")
    public LogisticaResponse buscarPorId(UUID id) 
    {
        log.info("Iniciando a busca de item de logística por ID {}.", id);
        Logistica entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item de logística não encontrado"));
        log.info("Item de logística com ID {} foi encontrada.", id);
        return mapper.toResponse(entity);
    }

    // =========================================================================
    // UPDATE
    // =========================================================================
    /**
    * Atualiza um item de logística existente buscando pelo ID.
    * @param id O UUID do item de logística a ser alterado.
    * @param request O DTO com os novos dados.
    * @return LogisticaResponse com os dados atualizados.
    * @throws ResourceNotFoundException se o item de logística não for encontrada.
    */
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "buscar-equipamentos", allEntries = true),
            @CacheEvict(value = "buscar-equipamento-por-id", key = "#id")
    })
    public LogisticaResponse atualizar(UUID id, LogisticaRequest request) 
    {
        log.info("Iniciando a atualização do item de logística com ID {}.", id);
        Logistica existente = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item de logística não encontrado"));

        Logistica atualizado = mapper.toEntity(request);
        atualizado.setId(existente.getId());
        atualizado.setOrganizacaoId(existente.getOrganizacaoId());
        atualizado.setMercadoriaId(existente.getMercadoriaId());
        Logistica atualizadoSalvo = repository.save(atualizado);
        log.info("Item de logística com ID {} alterado com sucesso.", id);
        return mapper.toResponse(atualizadoSalvo);
    }

    // =========================================================================
    // DELETE
    // =========================================================================
    /**
    * Elimina um item de logística pelo seu ID.
    * @param id O UUID do item de logística a ser excluída.
    * @throws ResourceNotFoundException se o item de logística não for encontrada.
    */
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "buscar-equipamentos", allEntries = true),
            @CacheEvict(value = "buscar-equipamento-por-id", key = "#id")
    })
    public void excluir(UUID id) 
    {
        log.info("Iniciando a exclusão do item de logística com ID {}.", id);
        Logistica entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item de logística não encontrado"));

        repository.delete(entity);
        log.info("Logistica com ID {} removida com sucesso", id);
    }
}