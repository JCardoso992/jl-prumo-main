package ao.prumo.obra.obramanagementservice.service;

import ao.prumo.obra.obramanagementservice.entity.ContaOrganizacao;
import ao.prumo.obra.obramanagementservice.entity.Despesa;
import ao.prumo.obra.obramanagementservice.entity.repository.DespesaRepository;
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
public class DespesaService
{

    private final DespesaRepository repository;
    private final DespesaMapper mapper;
    
    @Override
    protected JpaRepository<Despesa, UUID> getRepository()
    {
        return this.repository;
    }

    // =========================================================================
    // CREATE
    // =========================================================================
    @Transactional
    public DespesaResponse criarDespesa(DespesaRequest request) 
    {
        log.info("Iniciando a criação de uma nova despesa.");
        Despesa despesa = mapper.toEntity(request);
        return mapper.toResponse(repository.save(despesa));
    }

    // =========================================================================
    // READ - LIST (PAGINADO)
    // =========================================================================
    @Transactional(readOnly = true)
    public Page<DespesaResponse> listar(Pageable pageable) 
    {
        log.info("Iniciando a listagem de despesas.");
        return repository.findAll(pageable)
                .map(mapper::toResponse);
    }

    // =========================================================================
    // READ - BY ID
    // =========================================================================
    @Transactional(readOnly = true)
    public DespesaResponse buscarPorId(UUID id) 
    {
        log.info("Iniciando a busca de despesas por ID {}.");
        Despesa despesa = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Despesa não encontrada"));
        log.info("Despesa com ID {} foi encontrada.", id);
        return mapper.toResponse(despesa);
    }

    // =========================================================================
    // UPDATE
    // =========================================================================
    /**
    * Atualiza uma despesa existente buscando pelo ID.
    * @param id O UUID da despesa a ser alterada.
    * @param req O DTO com os novos dados.
    * @return DespesaResponse com os dados atualizados.
    * @throws ResourceNotFoundException se a despesa não for encontrada.
    */
    @Transactional
    public DespesaResponse atualizar(UUID id, DespesaRequest request) 
    {
        log.info("Iniciando a atualização da despesas com ID {}.", id);
        Despesa existente = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Despesa não encontrada"));

        Despesa atualizada = mapper.toEntity(request);
        atualizada.setId(existente.getId());
        Despesa atualizadaDespesa = repository.save(atualizada);
        log.info("Despesa com ID {} alterada com sucesso.", id);
        return mapper.toResponse(atualizadaDespesa);
    }

    // =========================================================================
    // DELETE
    // =========================================================================
    /**
    * Elimina uma despesa pelo seu ID.
    * @param id O UUID da despesa a ser excluída.
    * @throws ResourceNotFoundException se a despesa não for encontrada.
    */
    @Transactional
    public void excluir(UUID id) 
    {
        log.info("Iniciando a exclusão da despesa com ID {}.", id);
        Despesa despesa = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Despesa não encontrada"));
        repository.delete(despesa);
        log.info("Despesa com ID {} removida com sucesso", id);
    }
}