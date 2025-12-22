package ao.prumo.obra.obramanagementservice.service;

import ao.prumo.obra.obramanagementservice.entity.PagamentoProjecto;
import ao.prumo.obra.obramanagementservice.entity.dto.mapper.PagamentoProjectoMapper;
import ao.prumo.obra.obramanagementservice.entity.dto.request.PagamentoProjectoRequest;
import ao.prumo.obra.obramanagementservice.entity.dto.response.PagamentoProjectoResponse;
import ao.prumo.obra.obramanagementservice.entity.repository.PagamentoProjectoRepository;
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
public class PagamentoProjectoService 
{

    private final PagamentoProjectoRepository repository;
    private final PagamentoProjectoMapper mapper;

    protected JpaRepository<PagamentoProjecto, UUID> getRepository()
    {
        return this.repository;
    }

     // =========================================================================
    // CREATE
    // =========================================================================
    public PagamentoProjectoResponse criar(PagamentoProjectoRequest request)
    {
        log.info("Iniciando a criação de um pagamento de projecto");
        PagamentoProjecto entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }

    // =========================================================================
    // READ - LIST
    // =========================================================================
    @Transactional(readOnly = true)
    public Page<PagamentoProjectoResponse> listar(Pageable pageable)
    {
        log.info("Iniciando a listagem de pagamentos do projecto.");
        return repository.findAll(pageable)
                .map(mapper::toResponse);
    }

    // =========================================================================
    // READ - BY ID
    // =========================================================================
    @Transactional(readOnly = true)
    public PagamentoProjectoResponse buscarPorId(UUID id) 
    {
        log.info("Iniciando a busca de pagamento do projecto por ID {}.", id);
        PagamentoProjecto pagamento = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pagamento de projecto não encontrado"));
        log.info("Pagamento de projecto com ID {} foi encontrado.", id);
        return mapper.toResponse(pagamento);
    }

    // =========================================================================
    // UPDATE
    // =========================================================================
    /**
    * Atualiza pagamento do projecto existente buscando pelo ID.
    * @param id O UUID do pagamento do projecto a ser alterada.
    * @param request O DTO com os novos dados.
    * @return PagamentoProjectoResponse com os dados atualizados.
    * @throws ResourceNotFoundException se o pagamento do projecto não for encontrado.
    */
    @Transactional
    public PagamentoProjectoResponse atualizar(UUID id, PagamentoProjectoRequest request) 
    {
        log.info("Iniciando a atualização do pagamento do projecto com ID {}.", id);
        PagamentoProjecto existente = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pagamento de projecto não encontrado"));

        PagamentoProjecto atualizado = mapper.toEntity(request);
        atualizado.setId(existente.getId());
        PagamentoProjecto salvo = repository.save(atualizado);
        log.info("Pagamento do projecto com ID {} atualizado com sucesso.", id);
        return mapper.toResponse(salvo);
    }

    // =========================================================================
    // DELETE
    // =========================================================================
    /**
    * Elimina pagamento do projecto pelo seu ID.
    * @param id O UUID do pagamento do projecto a ser excluído.
    * @throws ResourceNotFoundException se o pagamento do projecto não for encontrado.
    */
    @Transactional
    public void excluir(UUID id) 
    {
        log.info("Iniciando a exclusão do pagamento do projecto com ID {}.", id);
        PagamentoProjecto pagamento = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pagamento de projecto não encontrado"));
        repository.delete(pagamento);
        log.info("Pagamento de projecto com ID {} removido com sucesso", id);
    }

}