package ao.prumo.obra.obramanagementservice.service;

import ao.prumo.obra.obramanagementservice.entity.Obra;
import ao.prumo.obra.obramanagementservice.entity.dto.mapper.ObraMapper;
import ao.prumo.obra.obramanagementservice.entity.dto.request.ObraRequest;
import ao.prumo.obra.obramanagementservice.entity.dto.response.ObraResponse;
import ao.prumo.obra.obramanagementservice.entity.repository.ObraRepository;
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
public class ObraService extends BaseService<Obra, UUID> {

    private final ObraRepository repository;
    private final ObraMapper mapper;

    protected JpaRepository<Obra, UUID> getRepository()
    {
        return this.repository;
    }

    // =========================================================================
    // CREATE
    // =========================================================================
    @Transactional
    public ObraResponse criar(ObraRequest request)
    {
        log.info("Iniciando a criação de uma nova obra");
        Obra obra = mapper.toEntity(request);
        return mapper.toResponse(repository.save(obra));
    }

    // =========================================================================
    // READ - LIST
    // =========================================================================
    @Transactional(readOnly = true)
    public Page<ObraResponse> listar(Pageable pageable)
    {
        log.info("Iniciando a listagem de obras.");
        return repository.findAll(pageable)
                .map(mapper::toResponse);
    }

    // =========================================================================
    // READ - BY ID
    // =========================================================================
    @Transactional(readOnly = true)
    public ObraResponse buscarPorId(UUID id) 
    {
        log.info("Iniciando a busca de obra por ID {}.", id);
        Obra obra = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Obra não encontrada"));
        log.info("Obra com ID {} foi encontrado.", id);        
        return mapper.toResponse(obra);
    }

    // =========================================================================
    // UPDATE
    // =========================================================================
    /**
     * Atualiza uma obra existente buscando pelo ID.
     * @param id O UUID da obra a ser alterado.
     * @param request O DTO com os novos dados.
     * @return ObraResponse com os dados atualizados.
     * @throws ResourceNotFoundException se a obra não for encontrada.
     */
    @Transactional
    public ObraResponse atualizar(UUID id, ObraRequest request) {
        log.info("Iniciando a atualização da obra com ID: {}", id);
        Obra existente = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Obra não encontrada"));

        Obra atualizado = mapper.toEntity(request);
        atualizado.setId(existente.getId());
        Obra atualizadoObra = repository.save(atualizado);
        log.info("Obra com ID {} atualizado com sucesso.", id);
        return mapper.toResponse(atualizadoObra);
    }

    // =========================================================================
    // DELETE
    // =========================================================================
    /**
     * Elimina uma obra pelo seu ID.
     * @param id O UUID da obra a ser excluído.
     * @throws ResourceNotFoundException se o obra não for encontrado.
     */
    public void excluir(UUID id) 
    {
        log.info("Iniciando a exclusão da obra com ID {}.", id);
        Obra obra = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Obra não encontrada"));
        repository.delete(obra);
        log.info("Obra com ID {} removida com sucesso", id);
    }
}