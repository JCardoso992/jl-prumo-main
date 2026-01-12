package ao.prumo.obra.obramanagementservice.service;

import ao.prumo.obra.obramanagementservice.entity.*;
import ao.prumo.obra.obramanagementservice.entity.dto.mapper.ClienteMapper;
import ao.prumo.obra.obramanagementservice.entity.dto.mapper.EnderecoMapper;
import ao.prumo.obra.obramanagementservice.entity.dto.mapper.PessoaMapper;
import ao.prumo.obra.obramanagementservice.entity.dto.mapper.ClienteViewMapper;
import ao.prumo.obra.obramanagementservice.entity.dto.request.ClienteRequest;
import ao.prumo.obra.obramanagementservice.entity.dto.request.EnderecoRequest;
import ao.prumo.obra.obramanagementservice.entity.dto.response.ClienteResponse;
import ao.prumo.obra.obramanagementservice.entity.dto.response.ClienteViewResponse;
import ao.prumo.obra.obramanagementservice.entity.repository.ClienteRepository;
import ao.prumo.obra.obramanagementservice.entity.repository.EnderecoRepository;
import ao.prumo.obra.obramanagementservice.entity.repository.PessoaRepository;
import ao.prumo.obra.obramanagementservice.entity.repository.ClienteViewRepository;
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
public class ClienteService
{

    private final ClienteRepository repository;
    private final PessoaRepository pessoaRepository;
    private final EnderecoRepository enderecoRepository;

    private final ClienteMapper clienteMapper;
    private final PessoaMapper pessoaMapper;
    private final EnderecoMapper enderecoMapper;

    private final FileStorageService fileService;

    private final ClienteViewRepository clienteViewRepository;
    private final ClienteViewMapper clienteViewMapper;

    protected JpaRepository<Cliente, UUID> getRepository()
    {
        return this.repository;
    }

    @Transactional
    @CacheEvict(value = "buscar-clientes", allEntries = true)
    public ClienteResponse criarCliente(ClienteRequest req, MultipartFile file, Jwt jwt)
    {
        log.info("Iniciando a criação de uma novo cliente.");
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
        // 1. Endereço 1 (obrigatório)
        EnderecoRequest endRequest1, endRequest2;
        endRequest1 = req.getCodPessoa().getCodAdress1();
        Endereco endereco1 = enderecoMapper.toEntity(endRequest1);
        Endereco endereco1Salvo = enderecoRepository.save(endereco1);

        // 2. Endereço 2 (opcional)
        Endereco endereco2Salvo = null;
        if (req.getCodPessoa().getCodAdress2() != null) {
            endRequest2 = req.getCodPessoa().getCodAdress2();
            Endereco endereco2 = enderecoMapper.toEntity(endRequest2);
            endereco2Salvo = enderecoRepository.save(endereco2);
        }

        // 3. Pessoa
        Pessoa pessoa = pessoaMapper.toEntity(req.getCodPessoa());
        pessoa.setAdress1(endereco1Salvo);
        pessoa.setAdress2(endereco2Salvo);
        pessoa.setStatus(Boolean.TRUE);
        pessoa.setOrganizacaoId(new Organizacao(organizacaoId));
        Pessoa pessoaSalva = pessoaRepository.save(pessoa);

        // 4. Cliente
        Cliente cliente = clienteMapper.toEntity(req);
        // idOrganização= bb1827b3-57b9-49ec-a775-a7f5a91b8297
        if (file != null && !file.isEmpty()) {
            final String filePath = fileService.saveFile(file, pessoa.getNome(), "Cliente");
            cliente.setArquivoPath(filePath);
        }
        cliente.setOrganizacaoId(new Organizacao(organizacaoId));
        cliente.setPessoaId(pessoaSalva); // Associa a pessoa recém-criada
        cliente.setStatus(Boolean.TRUE);
        Cliente clienteSalvo = repository.save(cliente);
        log.info("Cliente criado com sucesso.");
        return clienteMapper.toResponse(clienteSalvo);
    }

    /**
     * Atualiza um cliente existente buscando pelo ID.
     * @param id O UUID do cliente a ser alterado.
     * @param req O DTO com os novos dados.
     * @return ClienteResponse com os dados atualizados.
     * @throws ResourceNotFoundException se o cliente não for encontrada.
     */
    @Transactional
    @Caching(evict = {
        @CacheEvict(value = "buscar-clientes", allEntries = true),
        @CacheEvict(value = "buscar-clientes-detalhado", allEntries = true),
        @CacheEvict(value = "buscar-cliente-por-id", key = "#id")
    })
    public ClienteResponse alterarCliente(UUID id, ClienteRequest req, MultipartFile file) {
       log.info("Iniciando a atualização do cliente com ID: {}", id);

       // 1. Recuperar entidades
       Cliente clienteExistente = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));
    
      Pessoa pessoaExistente = clienteExistente.getPessoaId(); // Assume-se que a relação existe

       // 2. Atualizar Endereço 1
      if (pessoaExistente.getAdress1() != null) {
          enderecoMapper.updateEntityFromDto(req.getCodPessoa().getCodAdress1(), pessoaExistente.getAdress1());
          enderecoRepository.save(pessoaExistente.getAdress1());
      }

       // 3. Lógica Especial para Endereço 2 (Trata deleção e atualização)
       if (req.getCodPessoa().getCodAdress2() != null) {
          if (pessoaExistente.getAdress2() != null) {
              // Atualiza existente
             enderecoMapper.updateEntityFromDto(req.getCodPessoa().getCodAdress2(), pessoaExistente.getAdress2());
             enderecoRepository.save(pessoaExistente.getAdress2());
           } else {
                // Cria novo se não existia antes mas veio no Request
                Endereco novoEnd2 = enderecoMapper.toEntity(req.getCodPessoa().getCodAdress2());
                pessoaExistente.setAdress2(enderecoRepository.save(novoEnd2));
            }
        } else if (pessoaExistente.getAdress2() != null) {
        // Se no Request veio nulo mas no banco existia, deletamos
           Endereco endADeletar = pessoaExistente.getAdress2();
           pessoaExistente.setAdress2(null); // Remove referência primeiro
           enderecoRepository.delete(endADeletar);
        }

        // 4. Atualizar Pessoa (Corrigido erro da variável pessoaAtualizada)
        pessoaMapper.updateEntityFromDto(req.getCodPessoa(), pessoaExistente);
        pessoaRepository.save(pessoaExistente);

        // 5. Atualizar Cliente e Imagem
        clienteMapper.updateEntityFromDto(req, clienteExistente);
    
        if (file != null && !file.isEmpty()) {
          final String filePath = fileService.saveFile(file, pessoaExistente.getNome(), "Cliente");
          clienteExistente.setArquivoPath(filePath);
       }

       Cliente clienteSalvo = repository.save(clienteExistente);

       log.info("Cliente com ID {} atualizado com sucesso.", id);
       return clienteMapper.toResponse(clienteSalvo);
    }

    /**
     * Elimina um cliente pelo seu ID.
     * @param id O UUID do cliente a ser excluído.
     * @throws ResourceNotFoundException se a cliente não for encontrado.
     */
    @Transactional
    @Caching(evict = {
           @CacheEvict(value = "buscar-clientes", allEntries = true),
           @CacheEvict(value = "buscar-clientes-detalhado", allEntries = true),
           @CacheEvict(value = "buscar-cliente-por-id", key = "#id")
    })
    public void excluirCliente(UUID id) throws ResourceNotFoundException 
    {
        log.info("Iniciando a exclusão do cliente com ID {}.", id);
        // 1. Verificar se o cliente existe.
        // O método findById() é herdado do BaseService e já lança ResourceNotFoundException se não encontrar.
        Cliente cliente = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));
        // 2. Se o cliente foi encontrada, prosseguir com a exclusão.
        cliente.setStatus(Boolean.FALSE);
        repository.save(cliente);
        log.info("Cliente com ID {} excluído com sucesso.", id);
    }

    // =========================================================================
    // READ - LIST (PAGINADO)
    // =========================================================================
    @Transactional(readOnly = true)
    @Cacheable(value = "buscar-clientes")  
    public Page<ClienteResponse> listarClientes(Pageable pageable, Jwt jwt)
    {
        log.info("Iniciando a listagem de clientes.");
        // 1. Extrair o ID da organização do TOKEN
        Map<String, Object> appMetadata = jwt.getClaimAsMap("app_metadata");
        String orgIdToken = (String) appMetadata.get("org_id");

        if (orgIdToken == null) {
            log.error("Claims presentes no token: {}", jwt.getClaims()); // Isso ajuda a debugar no log
            throw new AccessDeniedException("Utilizador não vinculado a uma organização.");
        }
        UUID organizacaoId = UUID.fromString(orgIdToken);
        return repository.findByOrganizacaoId(organizacaoId, pageable).map(clienteMapper::toResponse);
    }

    // =========================================================================
    // READ - BY ID
    // =========================================================================
    @Transactional(readOnly = true)
    @Cacheable(value = "buscar-cliente-por-id", key = "#id")
    public ClienteResponse buscarClientePorId(UUID id, Jwt jwt) 
    {
        log.info("Iniciando a busca de cliente por ID {}.", id);
         // 1. Extrair o ID da organização do TOKEN
        Map<String, Object> appMetadata = jwt.getClaimAsMap("app_metadata");
        String orgIdToken = (String) appMetadata.get("org_id");

        if (orgIdToken == null) {
            log.error("Claims presentes no token: {}", jwt.getClaims()); // Isso ajuda a debugar no log
            throw new AccessDeniedException("Utilizador não vinculado a uma organização.");
        }
        UUID organizacaoId = UUID.fromString(orgIdToken);
        Cliente cliente = repository.findByIdAndOrganizacaoId(id, organizacaoId)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));
        log.info("Cliente com ID {} foi encontrado.", id);
        return clienteMapper.toResponse(cliente);
    }

    // =========================================================================
    // READ - LIST (PAGINADO)
    // =========================================================================
    @Transactional(readOnly = true)
    @Cacheable(value = "buscar-clientes-detalhado")
    public Page<ClienteViewResponse> listarClientesDetalhado(Pageable pageable, Jwt jwt)
    {
        log.info("Iniciando a listagem de clientes detalhados.");
        // 1. Extrair o ID da organização do TOKEN
        Map<String, Object> appMetadata = jwt.getClaimAsMap("app_metadata");
        String orgIdToken = (String) appMetadata.get("org_id");

        if (orgIdToken == null) {
            log.error("Claims presentes no token: {}", jwt.getClaims()); // Isso ajuda a debugar no log
            throw new AccessDeniedException("Utilizador não vinculado a uma organização.");
        }
        UUID organizacaoId = UUID.fromString(orgIdToken);
        return clienteViewRepository.findByOrganizacaoId(organizacaoId, pageable).map(clienteViewMapper::toResponse);
    }

}
