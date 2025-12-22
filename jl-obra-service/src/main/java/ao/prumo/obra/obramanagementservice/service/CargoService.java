package ao.prumo.obra.obramanagementservice.service;

import ao.prumo.obra.obramanagementservice.entity.Cargo;
import ao.prumo.obra.obramanagementservice.entity.dto.mapper.CargoMapper;
import ao.prumo.obra.obramanagementservice.entity.dto.request.CargoRequest;
import ao.prumo.obra.obramanagementservice.entity.dto.response.CargoResponse;
import ao.prumo.obra.obramanagementservice.entity.repository.CargoRepository;
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
public class CargoService
{

    private final CargoRepository repository;

    private final CargoMapper cargoMapper;

    protected JpaRepository<Cargo, UUID> getRepository()
    {
        return this.repository;
    }

    @Transactional
    public CargoResponse criarCargo(CargoRequest req)
    {
        log.info("Iniciando a criação de uma novo Cargo.");
        // Mapper converte o DTO para Entidade -> Service salva a Entidade
        // -> Mapper converte a Entidade salva para DTO de Resposta
        return cargoMapper.toResponse(repository.save(cargoMapper.toEntity(req)));
    }

    /**
     * Atualiza um novo cargo existente buscando pelo ID.
     * @param id O UUID do cargo a ser alterada.
     * @param req O DTO com os novos dados.
     * @return CargoResponse com os dados atualizados.
     * @throws ResourceNotFoundException se o cargo não for encontrada.
     */
    @Transactional
    public CargoResponse alterarCargo(UUID id, CargoRequest req)
    {
        log.info("Iniciando a atualização do cargo com ID {}.", id);
        // 1. Verifica se a cargo existe pelo ID
        Cargo cargoExistente = this.repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cargo Não Existe")); // herdado do BaseService, que lança ResourceNotFoundException
        // 2. Mapeia os dados do Request para a Entidade, ignorando o ID (pois o ID é imutável)
        Cargo cargoAtualizada = cargoMapper.toEntity(req);
        cargoAtualizada.setId(cargoExistente.getId()); // Garante que o ID original seja mantido
        // 3. Persiste a atualização no banco de dados
        Cargo cargoSalva = this.repository.save(cargoAtualizada); // herdado do BaseService
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
    public void excluirCargo(UUID id) throws ResourceNotFoundException 
    {
        log.info("Iniciando a exclusão do cargo com ID {}.", id);
        // 1. Verificar se o cargo existe.
        // O método findById() é herdado do BaseService e já lança ResourceNotFoundException se não encontrar.
        this.repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cargo Não Existe"));
        // 2. Se a cargo foi encontrada, prosseguir com a exclusão.
        this.repository.deleteById(id);

        log.info("Cargo com ID {} excluída com sucesso.", id);
    }

    
    @Transactional(readOnly = true)
    public Page<CargoResponse> listarCargos(Pageable pageable)
    {
        log.info("Iniciando a listagem de cargos.");
        return repository.findAll(pageable)
                .map(cargoMapper::toResponse);
    }

    
    @Transactional(readOnly = true)
    public CargoResponse buscarCargoPorId(UUID id) 
    {
        log.info("Iniciando a listagem do cargo por ID {}.", id);
        Cargo cargo = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cargo não encontrado"));
        log.info("Cargo com ID {} foi encontrado.", id);
        return cargoMapper.toResponse(cargo);
    }
}
