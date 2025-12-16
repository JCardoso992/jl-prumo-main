package ao.prumo.obra.obramanagementservice.service;

import ao.prumo.obra.obramanagementservice.entity.Cargo;
import ao.prumo.obra.obramanagementservice.entity.dto.mapper.CargoMapper;
import ao.prumo.obra.obramanagementservice.entity.dto.request.CargoRequest;
import ao.prumo.obra.obramanagementservice.entity.dto.response.CargoResponse;
import ao.prumo.obra.obramanagementservice.entity.repository.CargoRepository;
import ao.prumo.obra.obramanagementservice.utils.base.BaseService;
import jakarta.persistence.EntityNotFoundException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Getter
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CargoService extends BaseService<Cargo, UUID> {

    private final CargoRepository repository;

    private final CargoMapper cargoMapper;

    protected JpaRepository<Cargo, UUID> getRepository() {
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
     * @throws EntityNotFoundException se o cargo não for encontrada.
     */
    @Transactional
    public CargoResponse alterarCargo(UUID id, CargoRequest req)
    {
        // 1. Verifica se a cargo existe pelo ID
        Cargo cargoExistente = this.repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cargo Não Existe")); // herdado do BaseService, que lança EntityNotFoundException
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
     * @throws EntityNotFoundException se o cargo não for encontrado.
     */
    @Transactional
    public void excluirCargo(UUID id) throws EntityNotFoundException {
        // 1. Verificar se o cargo existe.
        // O método findById() é herdado do BaseService e já lança EntityNotFoundException se não encontrar.
        this.repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cargo Não Existe"));
        // 2. Se a cargo foi encontrada, prosseguir com a exclusão.
        this.repository.deleteById(id);

        log.info("Cargo com ID {} excluída com sucesso.", id);
    }
}
