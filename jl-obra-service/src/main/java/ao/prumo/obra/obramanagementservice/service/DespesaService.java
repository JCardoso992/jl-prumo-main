package ao.prumo.obra.obramanagementservice.service;

import ao.prumo.obra.obramanagementservice.entity.Despesa;
import ao.prumo.obra.obramanagementservice.entity.Organizacao;
import ao.prumo.obra.obramanagementservice.entity.dto.mapper.DespesaMapper;
import ao.prumo.obra.obramanagementservice.entity.dto.request.DespesaRequest;
import ao.prumo.obra.obramanagementservice.entity.dto.response.DespesaResponse;
import ao.prumo.obra.obramanagementservice.entity.repository.DespesaRepository;
import ao.prumo.obra.obramanagementservice.utils.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DespesaService
{

    private final DespesaRepository repository;
    private final DespesaMapper mapper;
    
    protected JpaRepository<Despesa, UUID> getRepository()
    {
        return this.repository;
    }

    // =========================================================================
    // CREATE
    // =========================================================================
    @Transactional
    @CacheEvict(value = "buscar-despesas", allEntries = true)
    public DespesaResponse criarDespesa(DespesaRequest request, Jwt jwt)
    {
        log.info("Iniciando a criação de uma nova despesa.");
        // 1. Extrair o ID da organização do TOKEN
        Map<String, Object> appMetadata = jwt.getClaimAsMap("app_metadata");
        String orgIdToken = (String) appMetadata.get("org_id");

        if (orgIdToken == null) {
            log.error("Claims presentes no token: {}", jwt.getClaims()); // Isso ajuda a debugar no log
            throw new AccessDeniedException("Utilizador não vinculado a uma organização.");
        }
        UUID organizacaoId = UUID.fromString(orgIdToken);
        Despesa despesa = mapper.toEntity(request);
        despesa.setStatus(Boolean.TRUE);
        despesa.setOrganizacaoId(new Organizacao(organizacaoId));
        log.info("Despesa criada com sucesso.");
        return mapper.toResponse(repository.save(despesa));
    }

    // =========================================================================
    // READ - LIST (PAGINADO)
    // =========================================================================
    @Transactional(readOnly = true)
    @Cacheable("buscar-despesas")
    public Page<DespesaResponse> listar(Pageable pageable, Jwt jwt)
    {
        log.info("Iniciando a listagem de despesas.");
         Map<String, Object> appMetadata = jwt.getClaimAsMap("app_metadata");
        String orgIdToken = (String) appMetadata.get("org_id");

        if (orgIdToken == null) {
            log.error("Claims presentes no token: {}", jwt.getClaims()); // Isso ajuda a debugar no log
            throw new AccessDeniedException("Utilizador não vinculado a uma organização.");
        }
        UUID organizacaoId = UUID.fromString(orgIdToken);
        return repository.findByOrganizacaoId(organizacaoId, pageable).map(mapper::toResponse);
    }

    // =========================================================================
    // READ - BY ID
    // =========================================================================
    @Transactional(readOnly = true)
    @Cacheable("buscar-despesa-por-id")
    public DespesaResponse buscarPorId(UUID id, Jwt jwt) 
    {
        log.info("Iniciando a busca de despesas por ID {}.", id);
         Map<String, Object> appMetadata = jwt.getClaimAsMap("app_metadata");
        String orgIdToken = (String) appMetadata.get("org_id");

        if (orgIdToken == null) {
            log.error("Claims presentes no token: {}", jwt.getClaims()); // Isso ajuda a debugar no log
            throw new AccessDeniedException("Utilizador não vinculado a uma organização.");
        }
        UUID organizacaoId = UUID.fromString(orgIdToken);
        Despesa despesa = repository.findByIdAndOrganizacaoId(id, organizacaoId)
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
    * @param request O DTO com os novos dados.
    * @return DespesaResponse com os dados atualizados.
    * @throws ResourceNotFoundException se a despesa não for encontrada.
    */
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "buscar-despesas", allEntries = true),
            @CacheEvict(value = "buscar-despesa-por-id", key = "#id")
    })
    public DespesaResponse atualizar(UUID id, DespesaRequest request) 
    {
        log.info("Iniciando a atualização da despesas com ID {}.", id);
        Despesa existente = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Despesa não encontrada"));
        // 2. Preserva campos que não devem mudar (Ex: Despesa Pai original se o request for nulo)
        // O MapStruct fará o merge dos campos de texto (abreviação, descrição)
        mapper.updateEntityFromDto(request, existente);
        // 3. Lógica para Despesa Pai (Somente se vier no request)
        Optional.ofNullable(request.getCodDespesaPai())
                .map(Despesa::new) // Cria nova instância de referência
                .ifPresent(existente:: setDespesaPaiId);
        // 4. Persistência
        Despesa atualizadaDespesa = repository.save(existente);
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
    @Caching(evict = {
            @CacheEvict(value = "buscar-despesas", allEntries = true),
            @CacheEvict(value = "buscar-despesa-por-id", key = "#id")
    })
    public void excluir(UUID id) 
    {
        log.info("Iniciando a exclusão da despesa com ID {}.", id);
        Despesa despesa = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Despesa não encontrada"));
        despesa.setStatus(Boolean.FALSE);        
        repository.save(despesa);
        log.info("Despesa com ID {} removida com sucesso", id);
    }
}