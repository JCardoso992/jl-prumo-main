package ao.prumo.obra.obramanagementservice.service;

import ao.prumo.obra.obramanagementservice.entity.Obra;
import ao.prumo.obra.obramanagementservice.entity.Organizacao;
import ao.prumo.obra.obramanagementservice.entity.repository.OrganizacaoRepository;
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
public class OrganizacaoService
{

    private final OrganizacaoRepository repository;
    private final OrganizacaoMapper mapper;

    @Override
    protected JpaRepository<Organizacao, UUID> getRepository() {
        return this.repository;
    }

    // =========================================================================
    // CREATE
    // =========================================================================
    @Transactional
    public OrganizacaoResponse criar(OrganizacaoRequest request) 
    {
        log.info("Iniciando a criação de uma nova organização");
        Organizacao organizacao = mapper.toEntity(request);
        return mapper.toResponse(repository.save(organizacao));
    }

    // =========================================================================
    // READ - LIST
    // =========================================================================
    @Transactional(readOnly = true)
    public Page<OrganizacaoResponse> listar(Pageable pageable) 
    {
        log.info("Iniciando a listagem de organizaçães.");
        return repository.findAll(pageable)
                .map(mapper::toResponse);
    }

    // =========================================================================
    // READ - BY ID
    // =========================================================================
    @Transactional(readOnly = true)
    public OrganizacaoResponse buscarPorId(UUID id) 
    {
        log.info("Iniciando a busca de organização por ID {}.", id);
        Organizacao organizacao = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Organização não encontrada"));
        log.info("Organização com ID {} foi encontrada.", id);
        return mapper.toResponse(organizacao);
    }

    // =========================================================================
    // UPDATE
    // =========================================================================
    /**
    * Atualiza uma organização existente buscando pelo ID.
    * @param id O UUID da organização a ser alterada.
    * @param req O DTO com os novos dados.
    * @return OrganizacaoResponse com os dados atualizados.
    * @throws ResourceNotFoundException se a organização não for encontrada.
    */
    @Transactional
    public OrganizacaoResponse atualizar(UUID id, OrganizacaoRequest request) 
    {
        log.info("Iniciando a atualização da organização com ID {}.", id);
        Organizacao existente = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Organização não encontrada"));

        Organizacao atualizado = mapper.toEntity(request);
        atualizado.setId(existente.getId());
        Organizacao atualizadoOrganizacao = repository.save(atualizado);
        log.info("Organização com ID {} alterada com sucesso.", id);
        return mapper.toResponse(atualizadoOrganizacao);
    }

    // =========================================================================
    // DELETE
    // =========================================================================
    /**
    * Elimina uma organização pelo seu ID.
    * @param id O UUID da organização a ser excluída.
    * @throws ResourceNotFoundException se a organização não for encontrada.
    */
    @Transactional
    public void excluir(UUID id) 
    {
        log.info("Iniciando a exclusão da organização com ID {}.", id);
        Organizacao organizacao = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Organização não encontrada"));
        repository.delete(organizacao);
        log.info("Organização com ID {} removida com sucesso", id);
    }

}