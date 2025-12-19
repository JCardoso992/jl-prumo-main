package ao.prumo.obra.obramanagementservice.service;

import ao.prumo.obra.obramanagementservice.entity.Cliente;
import ao.prumo.obra.obramanagementservice.entity.ContaOrganizacao;
import ao.prumo.obra.obramanagementservice.entity.repository.ContaOrganizacaoRepository;
import ao.prumo.obra.obramanagementservice.utils.base.BaseService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @Override
    protected JpaRepository<ContaOrganizacao, UUID> getRepository() 
    {
        return this.repository;
    }

    // =========================================================================
    // CREATE
    // =========================================================================
    @Transactional
    public ContaOrganizacaoResponse criarContaOrganizacao(ContaOrganizacaoRequest request) 
    {
        log.info("Iniciando a criação de uma nova ContaOrganizacao.");
        ContaOrganizacao entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }

    // =========================================================================
    // READ - LIST (PAGINADO)
    // =========================================================================
    @Transactional(readOnly = true)
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
     * @param req O DTO com os novos dados.
     * @return ContaOrganizacaoResponse com os dados atualizados.
     * @throws ResourceNotFoundException se a conta organizacao não for encontrada.
     */
    @Transactional
    public ContaOrganizacaoResponse atualizar(UUID id, ContaOrganizacaoRequest request) 
    {
        log.info("Iniciando a atualização da conta da organizacao com ID {}.", id);
        ContaOrganizacao existente = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Conta da organização não encontrada"));

        ContaOrganizacao atualizado = mapper.toEntity(request);
        atualizado.setId(existente.getId());
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
    public void excluir(UUID id) throws ResourceNotFoundException  
    {
        log.info("Iniciando a exclusão da conta da organizacao com ID {}.", id);
        ContaOrganizacao entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Conta da organização não encontrada"));
        repository.delete(entity);
        log.info("ContaOrganizacao com ID {} removida com sucesso", id);
    }
}