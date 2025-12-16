package ao.prumo.obra.obramanagementservice.service;

import ao.prumo.obra.obramanagementservice.entity.Agencia;
import ao.prumo.obra.obramanagementservice.entity.dto.mapper.AgenciaMapper;
import ao.prumo.obra.obramanagementservice.entity.dto.request.AgenciaRequest;
import ao.prumo.obra.obramanagementservice.entity.dto.response.AgenciaResponse;
import ao.prumo.obra.obramanagementservice.entity.repository.AgenciaRepository;
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
public class AgenciaService{

   private final AgenciaRepository repository;

   private final AgenciaMapper agenciaMapper;

   protected JpaRepository<Agencia, UUID> getRepository() {
      return this.repository;
   }

   @Transactional
   public AgenciaResponse criarAgencia(AgenciaRequest req)
   {
      log.info("Iniciando a criação de uma nova agência.");
      // Mapper converte o DTO para Entidade -> Service salva a Entidade
      // -> Mapper converte a Entidade salva para DTO de Resposta
      return agenciaMapper.toResponse(repository.save(agenciaMapper.toEntity(req)));
   }

   /**
    * Atualiza uma agência existente buscando pelo ID.
    * @param id O UUID da agência a ser alterada.
    * @param req O DTO com os novos dados.
    * @return AgenciaResponse com os dados atualizados.
    * @throws EntityNotFoundException se a agência não for encontrada.
    */
   @Transactional
   public AgenciaResponse alterarAgencia(UUID id, AgenciaRequest req)
   {
      // 1. Verifica se a agência existe pelo ID
      Agencia agenciaExistente = this.repository.findById(id)
              .orElseThrow(() -> new EntityNotFoundException("Agencia Não Existe")); // herdado do BaseService, que lança EntityNotFoundException
      // 2. Mapeia os dados do Request para a Entidade, ignorando o ID (pois o ID é imutável)
      Agencia agenciaAtualizada = agenciaMapper.toEntity(req);
      agenciaAtualizada.setId(agenciaExistente.getId()); // Garante que o ID original seja mantido
      // 3. Persiste a atualização no banco de dados
      Agencia agenciaSalva = this.repository.save(agenciaAtualizada); // herdado do BaseService
      log.info("Agência com ID {} alterada com sucesso.", id);
      // 4. Converte e retorna o DTO de resposta
      return agenciaMapper.toResponse(agenciaSalva);
   }

   /**
    * Elimina uma agência pelo seu ID.
    * @param id O UUID da agência a ser excluída.
    * @throws EntityNotFoundException se a agência não for encontrada.
    */
   @Transactional
   public void excluirAgencia(UUID id) throws EntityNotFoundException {
      // 1. Verificar se a agência existe.
      // O método findById() é herdado do BaseService e já lança EntityNotFoundException se não encontrar.
      this.repository.findById(id)
              .orElseThrow(() -> new EntityNotFoundException("Agencia Não Existe"));
      // 2. Se a agência foi encontrada, prosseguir com a exclusão.
      this.repository.deleteById(id);

      log.info("Agência com ID {} excluída com sucesso.", id);
   }
}
