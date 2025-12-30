package ao.prumo.obra.obramanagementservice.service;

import ao.prumo.obra.obramanagementservice.entity.Agencia;
import ao.prumo.obra.obramanagementservice.entity.dto.mapper.AgenciaMapper;
import ao.prumo.obra.obramanagementservice.entity.dto.request.AgenciaRequest;
import ao.prumo.obra.obramanagementservice.entity.dto.response.AgenciaResponse;
import ao.prumo.obra.obramanagementservice.entity.repository.AgenciaRepository;
import ao.prumo.obra.obramanagementservice.file.FileStorageService;
import ao.prumo.obra.obramanagementservice.utils.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AgenciaService{

   private final AgenciaRepository repository;

   private final AgenciaMapper agenciaMapper;

   private final FileStorageService fileService;

   protected JpaRepository<Agencia, UUID> getRepository()
   {
      return this.repository;
   }

   @Transactional
   @CacheEvict(value = "buscar-agencias", allEntries = true)
   public AgenciaResponse criarAgencia(AgenciaRequest req, MultipartFile file)
   {
      log.info("Iniciando a criação de uma nova agência.");
      // Mapper converte o DTO para Entidade -> Service salva a Entidade
      // -> Mapper converte a Entidade salva para DTO de Resposta
      Agencia entity = agenciaMapper.toEntity(req);
      if(file != null && !file.isEmpty())
      {
        final String filePath = fileService.saveFile(file, req.getAbreviacao(), "Agencia");
        entity.setArquivoPath(filePath);
      }
      Agencia entitySalva = repository.save(entity);
      log.info("Agência criada com sucesso.");
      return agenciaMapper.toResponse(entitySalva);
   }

   /**
    * Atualiza uma agência existente buscando pelo ID.
    * @param id O UUID da agência a ser alterada.
    * @param req O DTO com os novos dados.
    * @return AgenciaResponse com os dados atualizados.
    * @throws ResourceNotFoundException se a agência não for encontrada.
    */
   @Transactional
   @Caching(evict = {
           @CacheEvict(value = "buscar-agencias", allEntries = true),
           @CacheEvict(value = "buscar-agencia-por-id", key = "#id")
   })
   public AgenciaResponse alterarAgencia(UUID id, AgenciaRequest req, MultipartFile file)
   {
      log.info("Iniciando a atualização da agencia com ID {}.", id);
      // 1. Verifica se a agência existe pelo ID
      Agencia agenciaExistente = this.repository.findById(id)
              .orElseThrow(() -> new ResourceNotFoundException("Agencia Não Existe")); // herdado do BaseService, que lança ResourceNotFoundException
      // 2. Mapeia os dados do Request para a Entidade, ignorando o ID (pois o ID é imutável)
      Agencia agenciaAtualizada = agenciaMapper.toEntity(req);
      agenciaAtualizada.setId(agenciaExistente.getId()); // Garante que o ID original seja mantido
      // 3. Persiste a atualização no banco de dados
      if(file != null)
      {
         final String filePath = fileService.saveFile(file, req.getAbreviacao(), "Agencia");
         agenciaAtualizada.setArquivoPath(filePath);
      }else{
         agenciaAtualizada.setArquivoPath(agenciaExistente.getArquivoPath());
      }

      Agencia agenciaSalva = this.repository.save(agenciaAtualizada); // herdado do BaseService
      log.info("Agência com ID {} alterada com sucesso.", id);
      // 4. Converte e retorna o DTO de resposta
      return agenciaMapper.toResponse(agenciaSalva);
   }

   /**
    * Elimina uma agência pelo seu ID.
    * @param id O UUID da agência a ser excluída.
    * @throws ResourceNotFoundException se a agência não for encontrada.
    */
   @Transactional
   @Caching(evict = {
           @CacheEvict(value = "buscar-agencias", allEntries = true),
           @CacheEvict(value = "buscar-agencia-por-id", key = "#id")
   })
   public void excluirAgencia(UUID id) throws ResourceNotFoundException 
   {
      log.info("Iniciando a exclusão da agencia com ID {}.", id);
      // 1. Verificar se a agência existe.
      // O método findById() é herdado do BaseService e já lança ResourceNotFoundException se não encontrar.
      this.repository.findById(id)
              .orElseThrow(() -> new ResourceNotFoundException("Agencia Não Existe"));
      // 2. Se a agência foi encontrada, prosseguir com a exclusão.
      this.repository.deleteById(id);

      log.info("Agência com ID {} excluída com sucesso.", id);
   }

   @Transactional(readOnly = true)
   @Cacheable("buscar-agencias")
   public Page<AgenciaResponse> listar(Pageable pageable)
   {
      log.info("Iniciando a listagem de agencias.");
      return repository.findAll(pageable)
            .map(agenciaMapper::toResponse);
   }

   @Transactional(readOnly = true)
   @Cacheable("buscar-agencia-por-id")
   public AgenciaResponse buscarPorId(UUID id) 
   {
      log.info("Iniciando a busca de agencia por ID {}.", id);
      Agencia agencia = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Agência não encontrada"));
      log.info("Agencia com ID {} foi encontrada.", id);
      return agenciaMapper.toResponse(agencia);
   }

}
