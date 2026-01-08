package ao.prumo.obra.obramanagementservice.service;

import ao.prumo.obra.obramanagementservice.entity.Cargo;
import ao.prumo.obra.obramanagementservice.entity.Organizacao;
import ao.prumo.obra.obramanagementservice.entity.dto.mapper.CargoMapper;
import ao.prumo.obra.obramanagementservice.entity.dto.request.CargoRequest;
import ao.prumo.obra.obramanagementservice.entity.dto.response.CargoResponse;
import ao.prumo.obra.obramanagementservice.entity.repository.CargoRepository;
import ao.prumo.obra.obramanagementservice.utils.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CargoService
{

    private final CargoRepository repository;

    private final CargoMapper cargoMapper;

    protected JpaRepository<Cargo, UUID> getRepository()
    {
        return this.repository;
    }

    @Transactional
    @CacheEvict(value = "buscar-cargos", allEntries = true)
    public CargoResponse criarCargo(CargoRequest req, Jwt jwt)
    {
        log.info("Iniciando a criação de uma novo Cargo.");
        // 1. Extrair o ID da organização do TOKEN
        Map<String, Object> appMetadata = jwt.getClaimAsMap("app_metadata");
        String orgIdToken = (String) appMetadata.get("org_id");

        if (orgIdToken == null) {
            log.error("Claims presentes no token: {}", jwt.getClaims()); // Isso ajuda a debugar no log
            throw new AccessDeniedException("Utilizador não vinculado a uma organização.");
        }
        UUID organizacaoId = UUID.fromString(orgIdToken);
        // Mapper converte o DTO para Entidade -> Service salva a Entidade
        // -> Mapper converte a Entidade salva para DTO de Resposta
        Cargo entity = cargoMapper.toEntity(req);
        entity.setStatus(Boolean.TRUE);
        entity.setOrganizacaoId(new Organizacao(organizacaoId));
        Cargo entitySalva = repository.save(entity);
        log.info("Cargo criada com sucesso.");
        return cargoMapper.toResponse(entitySalva);
    }

    /**
     * Atualiza um novo cargo existente buscando pelo ID.
     * @param id O UUID do cargo a ser alterada.
     * @param req O DTO com os novos dados.
     * @return CargoResponse com os dados atualizados.
     * @throws ResourceNotFoundException se o cargo não for encontrada.
     */
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "buscar-cargos", allEntries = true),
            @CacheEvict(value = "buscar-cargos-por-id", key = "#id")
    })
    public CargoResponse alterarCargo(UUID id, CargoRequest req)
    {
        log.info("Iniciando a atualização do cargo com ID {}.", id);
        // 1. Verifica se a cargo existe pelo ID
        Cargo cargoExistente = this.repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cargo Não Existe")); // herdado do BaseService, que lança ResourceNotFoundException
        // 2. Mapeia os dados do Request para a Entidade, ignorando o ID (pois o ID é imutável)
        cargoMapper.updateEntityFromDto(req, cargoExistente);
        // 3. Persiste a atualização no banco de dados
        Cargo cargoSalva = this.repository.save(cargoExistente); // herdado do BaseService
        log.info("Cargo com ID {} alterada com sucesso.", id);
        // 4. Converte e retorna o DTO de resposta
        return cargoMapper.toResponse(cargoSalva);
    }

    /**
     * Elimina um cargo pelo seu ID.
     * @param id O UUID do cargo a ser excluído.
     * @throws ResourceNotFoundException se o cargo não for encontrado.
     */
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "buscar-cargos", allEntries = true),
            @CacheEvict(value = "buscar-cargo-por-id", key = "#id")
    })
    public void excluirCargo(UUID id) throws ResourceNotFoundException 
    {
        log.info("Iniciando a exclusão do cargo com ID {}.", id);
        // 1. Verificar se o cargo existe.
        // O método findById() é herdado do BaseService e já lança ResourceNotFoundException se não encontrar.
        Cargo cargoExistente =  this.repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cargo Não Existe"));
        // 2. Se a cargo foi encontrada, prosseguir com a exclusão.
        cargoExistente.setStatus(Boolean.FALSE);
        this.repository.save(cargoExistente);

        log.info("Cargo com ID {} excluída com sucesso.", id);
    }

    
    @Transactional(readOnly = true)
    @Cacheable("buscar-cargos")
    public Page<CargoResponse> listarCargos(Pageable pageable, Jwt jwt)
    {
        log.info("Iniciando a listagem de cargos.");
        // 1. Extrair o ID da organização do TOKEN
        Map<String, Object> appMetadata = jwt.getClaimAsMap("app_metadata");
        String orgIdToken = (String) appMetadata.get("org_id");

        if (orgIdToken == null) {
            log.error("Claims presentes no token: {}", jwt.getClaims()); // Isso ajuda a debugar no log
            throw new AccessDeniedException("Utilizador não vinculado a uma organização.");
        }
        UUID organizacaoId = UUID.fromString(orgIdToken);
        return repository.findAll(pageable)
                .map(cargoMapper::toResponse);
    }

    
    @Transactional(readOnly = true)
    @Cacheable("buscar-cargo-por-id")
    public CargoResponse buscarCargoPorId(UUID id, Jwt jwt) 
    {
        log.info("Iniciando a listagem do cargo por ID {}.", id);
        // 1. Extrair o ID da organização do TOKEN
        Map<String, Object> appMetadata = jwt.getClaimAsMap("app_metadata");
        String orgIdToken = (String) appMetadata.get("org_id");

        if (orgIdToken == null) {
            log.error("Claims presentes no token: {}", jwt.getClaims()); // Isso ajuda a debugar no log
            throw new AccessDeniedException("Utilizador não vinculado a uma organização.");
        }
        UUID organizacaoId = UUID.fromString(orgIdToken);
        Cargo cargo = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cargo não encontrado"));
        log.info("Cargo com ID {} foi encontrado.", id);
        return cargoMapper.toResponse(cargo);
    }
}
