package ao.prumo.obra.obramanagementservice.service;

import ao.prumo.obra.obramanagementservice.entity.EtapaObra;
import ao.prumo.obra.obramanagementservice.entity.dto.mapper.EtapaObraMapper;
import ao.prumo.obra.obramanagementservice.entity.dto.request.EtapaObraRequest;
import ao.prumo.obra.obramanagementservice.entity.dto.response.EtapaObraResponse;
import ao.prumo.obra.obramanagementservice.entity.repository.EtapaObraRepository;
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
public class EtapaObraService
{

    private final EtapaObraRepository repository;
    private final EtapaObraMapper mapper;

    protected JpaRepository<EtapaObra, UUID> getRepository()
    {
        return this.repository;
    }

    // =========================================================================
    // CREATE
    // =========================================================================
    @Transactional
    public EtapaObraResponse criar(EtapaObraRequest request)
    {
        log.info("Iniciando a criação de uma nova Etapa da Obra");
        EtapaObra entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }

    // =========================================================================
    // READ - LIST (PAGINADO)
    // =========================================================================
    @Transactional(readOnly = true)
    public Page<EtapaObraResponse> listar(Pageable pageable)
    {
        log.info("Iniciando a listagem de etapa da obra.");
        return repository.findAll(pageable).map(mapper::toResponse);
    }

    // =========================================================================
    // READ - BY ID
    // =========================================================================
    @Transactional(readOnly = true)
    public EtapaObraResponse buscarPorId(UUID id) 
    {
        log.info("Iniciando a busca da etapa da obra por ID {}.", id);
        EtapaObra etapa = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Etapa da obra não encontrada"));
        log.info("Etapa da obra com ID {} foi encontrada.", id);
        return mapper.toResponse(etapa);
    }

    // =========================================================================
    // UPDATE
    // =========================================================================
    /**
    * Atualiza uma Etapa da Obra existente buscando pelo ID.
    * @param id O UUID da etapa da obra a ser alterada.
    * @param request O DTO com os novos dados.
    * @return EtapaObraResponse com os dados atualizados.
    * @throws ResourceNotFoundException se a etapa da obra não for encontrada.
    */
    @Transactional
    public EtapaObraResponse atualizar(UUID id, EtapaObraRequest request) 
    {
        log.info("Iniciando a atualização da etapa da obra com ID {}.", id);
        EtapaObra existente = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Etapa da obra não encontrada"));

        EtapaObra atualizado = mapper.toEntity(request);
        atualizado.setId(existente.getId());
        EtapaObra atualizadoSalvo = repository.save(atualizado);
        log.info("Etapa da obra com ID {} alterada com sucesso.", id);
        return mapper.toResponse(atualizadoSalvo);
    }

    // =========================================================================
    // DELETE
    // =========================================================================
    /**
    * Elimina uma Etapa da obra pelo seu ID.
    * @param id O UUID da etapa da obra a ser excluída.
    * @throws ResourceNotFoundException se a agência não for encontrada.
    */
   @Transactional
    public void excluir(UUID id) 
    {
        log.info("Iniciando a exclusão da etapa da obra com ID {}.", id);
        EtapaObra etapa = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Etapa da obra não encontrada"));
        repository.delete(etapa);
        log.info("EtapaObra com ID {} removida com sucesso", id);
    }
}