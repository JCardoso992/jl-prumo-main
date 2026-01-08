package ao.prumo.obra.obramanagementservice.service;

import ao.prumo.obra.obramanagementservice.entity.Endereco;
import ao.prumo.obra.obramanagementservice.entity.Organizacao;
import ao.prumo.obra.obramanagementservice.entity.Perfil;
import ao.prumo.obra.obramanagementservice.entity.UserRole;
import ao.prumo.obra.obramanagementservice.entity.dto.mapper.OrganizacaoMapper;
import ao.prumo.obra.obramanagementservice.entity.dto.mapper.EnderecoMapper;
import ao.prumo.obra.obramanagementservice.entity.dto.request.OrganizacaoRequest;
import ao.prumo.obra.obramanagementservice.entity.dto.response.OrganizacaoResponse;
import ao.prumo.obra.obramanagementservice.entity.repository.OrganizacaoRepository;
import ao.prumo.obra.obramanagementservice.entity.repository.EnderecoRepository;
import ao.prumo.obra.obramanagementservice.entity.repository.PerfilRepository;
import ao.prumo.obra.obramanagementservice.entity.dto.request.EnderecoRequest;
import ao.prumo.obra.obramanagementservice.entity.dto.request.FuncionarioRequest;
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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrganizacaoService
{

    private final OrganizacaoRepository repository;
    private final EnderecoRepository enderecoRepository;
    private final PerfilRepository perfilRepository;

    private final OrganizacaoMapper mapper;
    private final EnderecoMapper enderecoMapper;

    private final FileStorageService fileService;

    // Adicione o JdbcTemplate para interagir com a tabela de perfis do Supabase
   /// private final JdbcTemplate jdbcTemplate;

    protected JpaRepository<Organizacao, UUID> getRepository() {
        return this.repository;
    }

    // =========================================================================
    // CREATE
    // =========================================================================
    @Transactional
    @CacheEvict(value = "buscar-organizacao", allEntries = true)
    public OrganizacaoResponse criar(OrganizacaoRequest request, MultipartFile file, Jwt jwt)
    {
        log.info("Iniciando a criação de uma nova organização");
        // 1. Endereço 1 (obrigatório)
        EnderecoRequest endRequest = request.getCodAdress();
        Endereco endereco = enderecoMapper.toEntity(endRequest);
        endereco.setStatus(Boolean.TRUE);
        Endereco enderecoSalvo = enderecoRepository.save(endereco);

        
        Organizacao organizacao = mapper.toEntity(request);
        final String filePath = fileService.saveFile(file, request.getFirma(), "Organizacao");
        organizacao.setArquivoPath(filePath);
        organizacao.setStatus(Boolean.TRUE);
        organizacao.setAdress(enderecoSalvo);

        Organizacao organizacaoCriada = repository.saveAndFlush(organizacao);
        // 2. VINCULAR O UTILIZADOR À ORGANIZAÇÃO NO SUPABASE
        // Convertemos explicitamente para UUID para o PostgreSQL não reclamar do tipo de dados
        // 2. Obtém o UUID do usuário do Token
        UUID usuarioId = UUID.fromString(jwt.getSubject());

        // Chamada direta ao método com SQL Nativo para evitar erro de tipo
        perfilRepository.atualizarPerfilComoAdmin(
        organizacaoCriada.getId(), "ADMIN", true,  usuarioId);

        log.info("Organização criada com sucesso.");
        return mapper.toResponse(organizacaoCriada);
    }

    // =========================================================================
    // READ - LIST
    // =========================================================================
    @Transactional(readOnly = true)
    @Cacheable(value = "buscar-organizacao")
    public Page<OrganizacaoResponse> listar(Pageable pageable, Jwt jwt)
    {
        log.info("Iniciando a listagem de organizaçães.");
        // 1. Extrair o ID da organização do TOKEN
        Map<String, Object> appMetadata = jwt.getClaimAsMap("app_metadata");
        String orgIdToken = (String) appMetadata.get("org_id");

        if (orgIdToken == null) {
            log.error("Claims presentes no token: {}", jwt.getClaims()); // Isso ajuda a debugar no log
            throw new AccessDeniedException("Utilizador não vinculado a uma organização.");
        }
        UUID organizacaoId = UUID.fromString(orgIdToken);
        return repository.findAll(pageable)
                .map(mapper::toResponse);
    }

    // =========================================================================
    // READ - BY ID
    // =========================================================================
    @Transactional(readOnly = true)
    @Cacheable(value = "buscar-organizacao-por-id", key = "#id")
    public OrganizacaoResponse buscarPorId(UUID id, Jwt jwt)
    {
        log.info("Iniciando a busca de organização por ID {}.", id);
        // 1. Extrair o ID da organização do TOKEN
        Map<String, Object> appMetadata = jwt.getClaimAsMap("app_metadata");
        String orgIdToken = (String) appMetadata.get("org_id");

        if (orgIdToken == null) {
            log.error("Claims presentes no token: {}", jwt.getClaims()); // Isso ajuda a debugar no log
            throw new AccessDeniedException("Utilizador não vinculado a uma organização.");
        }
        UUID organizacaoId = UUID.fromString(orgIdToken);
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
    * @param request O DTO com os novos dados.
    * @return OrganizacaoResponse com os dados atualizados.
    * @throws ResourceNotFoundException se a organização não for encontrada.
    */
    @Transactional
    @Caching(evict = {
           @CacheEvict(value = "buscar-organizacao", allEntries = true),
           @CacheEvict(value = "buscar-organizacao-por-id", key = "#id")
    })
    public OrganizacaoResponse atualizar(UUID id, OrganizacaoRequest request, MultipartFile file) 
    {
        log.info("Iniciando a atualização da organização com ID {}.", id);
        Organizacao existente = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Organização não encontrada"));

        Endereco enderecoExistente = existente.getAdress();

        // Atualizar Endereço 1
        enderecoMapper.updateEntityFromDto(request.getCodAdress(), enderecoExistente);
        enderecoRepository.save(enderecoExistente);

        mapper.updateEntityFromDto(request, existente);
        if(file != null)
        {
          final String filePath = fileService.saveFile(file, request.getFirma(), "Organizacao");
          existente.setArquivoPath(filePath);
        }
        Organizacao atualizadoOrganizacao = repository.save(existente);
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
    @Caching(evict = {
           @CacheEvict(value = "buscar-organizacao", allEntries = true),
           @CacheEvict(value = "buscar-organizacao-por-id", key = "#id")
    })
    public void excluir(UUID id) 
    {
        log.info("Iniciando a exclusão da organização com ID {}.", id);
        Organizacao organizacao = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Organização não encontrada"));
        repository.delete(organizacao);
        log.info("Organização com ID {} removida com sucesso", id);
    }

}