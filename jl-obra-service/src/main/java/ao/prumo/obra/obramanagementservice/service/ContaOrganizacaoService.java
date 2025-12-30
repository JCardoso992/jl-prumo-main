package ao.prumo.obra.obramanagementservice.service;

import ao.prumo.obra.obramanagementservice.entity.Agencia;
import ao.prumo.obra.obramanagementservice.entity.ContaOrganizacao;
import ao.prumo.obra.obramanagementservice.entity.Organizacao;
import ao.prumo.obra.obramanagementservice.entity.dto.mapper.ContaOrganizacaoMapper;
import ao.prumo.obra.obramanagementservice.entity.dto.request.ContaOrganizacaoRequest;
import ao.prumo.obra.obramanagementservice.entity.dto.response.ContaOrganizacaoResponse;
import ao.prumo.obra.obramanagementservice.entity.repository.ContaOrganizacaoRepository;
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
public class ContaOrganizacaoService
{

    private final ContaOrganizacaoRepository repository;
    private final ContaOrganizacaoMapper mapper;

    protected JpaRepository<ContaOrganizacao, UUID> getRepository()
    {
        return this.repository;
    }

    // =========================================================================
    // CREATE
    // =========================================================================
    @Transactional
    @CacheEvict(value = "buscar-contas", allEntries = true)
    public ContaOrganizacaoResponse criarContaOrganizacao(ContaOrganizacaoRequest request)
    {
        log.info("Iniciando a criação de uma nova ContaOrganizacao.");
        // idOrganização= bb1827b3-57b9-49ec-a775-a7f5a91b8297
        ContaOrganizacao entity = mapper.toEntity(request);
        entity.setOrganicacaoId(new Organizacao(UUID.fromString("bb1827b3-57b9-49ec-a775-a7f5a91b8297")));
        entity.setAgenciaId(new Agencia(request.getCodAgencia()));
        entity.setStatus(Boolean.TRUE);
        ContaOrganizacao salvo = repository.save(entity);
        log.info("ContaOrganizacao criada com sucesso com ID {}.", salvo.getId());
        return mapper.toResponse(salvo);
    }

    // =========================================================================
    // READ - LIST (PAGINADO)
    // =========================================================================
    @Transactional(readOnly = true)
    @Cacheable("buscar-contas")
    public Page<ContaOrganizacaoResponse> listar(Pageable pageable)
    {
        log.info("Iniciando a listagem de contas da organizacao.");
        return repository.findAll(pageable)
                .map(mapper::toResponse);
    }

    // =========================================================================
    // READ - BY ID
    // =========================================================================
    @Transactional(readOnly = true)
    @Cacheable(value = "buscar-conta-por-id", key = "#id")
    public ContaOrganizacaoResponse buscarPorId(UUID id) 
    {
        log.info("Iniciando a busca da conta da organizacao por ID {}.", id);
        ContaOrganizacao entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Conta da organização não encontrada"));
        log.info("ContaOrganizacao com ID {} foi encontrada.", id);        
        return mapper.toResponse(entity);
    }

    // =========================================================================
    // UPDATE
    // =========================================================================
    /**
     * Atualiza um conta organizacao existente buscando pelo ID.
     * @param id O UUID da conta organizacao a ser alterado.
     * @param request O DTO com os novos dados.
     * @return ContaOrganizacaoResponse com os dados atualizados.
     * @throws ResourceNotFoundException se a conta organizacao não for encontrada.
     */
    @Transactional
    @Caching(evict = {
           @CacheEvict(value = "buscar-contas", allEntries = true),
           @CacheEvict(value = "buscar-conta-por-id", key = "#id")
    })
    public ContaOrganizacaoResponse atualizar(UUID id, ContaOrganizacaoRequest request) 
    {
        log.info("Iniciando a atualização da conta da organizacao com ID {}.", id);
        ContaOrganizacao existente = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Conta da organização não encontrada"));

        ContaOrganizacao atualizado = mapper.toEntity(request);
        atualizado.setId(existente.getId());
        if(request.getCodAgencia() != null)
        {
            atualizado.setAgenciaId(new Agencia(request.getCodAgencia()));
        } else {
            atualizado.setAgenciaId(existente.getAgenciaId());
        }
        atualizado.setOrganicacaoId(existente.getOrganicacaoId());
        atualizado.setStatus(Boolean.TRUE);
        ContaOrganizacao atualizadoSalvo = repository.save(atualizado);
        log.info("Agência com ID {} alterada com sucesso.", id);
        return mapper.toResponse(atualizadoSalvo);
    }

    // =========================================================================
    // DELETE
    // =========================================================================
     /**
     * Elimina um conta organizacao pelo seu ID.
     * @param id O UUID da conta organizacao a ser excluído.
     * @throws ResourceNotFoundException se a conta organizacao não for encontrado.
     */
    @Transactional
    @Caching(evict = {
           @CacheEvict(value = "buscar-contas", allEntries = true),
           @CacheEvict(value = "buscar-conta-por-id", key = "#id")
    })
    public void excluir(UUID id) throws ResourceNotFoundException  
    {
        log.info("Iniciando a exclusão da conta da organizacao com ID {}.", id);
        ContaOrganizacao entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Conta da organização não encontrada"));
        repository.delete(entity);
        log.info("ContaOrganizacao com ID {} removida com sucesso", id);
    }
}