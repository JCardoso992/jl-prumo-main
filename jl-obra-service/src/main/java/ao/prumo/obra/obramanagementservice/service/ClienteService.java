package ao.prumo.obra.obramanagementservice.service;

import ao.prumo.obra.obramanagementservice.entity.*;
import ao.prumo.obra.obramanagementservice.entity.dto.mapper.ClienteMapper;
import ao.prumo.obra.obramanagementservice.entity.dto.mapper.EnderecoMapper;
import ao.prumo.obra.obramanagementservice.entity.dto.mapper.PessoaMapper;
import ao.prumo.obra.obramanagementservice.entity.dto.request.ClienteRequest;
import ao.prumo.obra.obramanagementservice.entity.dto.request.EnderecoRequest;
import ao.prumo.obra.obramanagementservice.entity.dto.response.ClienteResponse;
import ao.prumo.obra.obramanagementservice.entity.repository.ClienteRepository;
import ao.prumo.obra.obramanagementservice.entity.repository.EnderecoRepository;
import ao.prumo.obra.obramanagementservice.entity.repository.PessoaRepository;
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
public class ClienteService
{

    private final ClienteRepository repository;
    private final PessoaRepository pessoaRepository;
    private final EnderecoRepository enderecoRepository;

    private final ClienteMapper clienteMapper;
    private final PessoaMapper pessoaMapper;
    private final EnderecoMapper enderecoMapper;

    private final FileStorageService fileService;

    protected JpaRepository<Cliente, UUID> getRepository()
    {
        return this.repository;
    }

    @Transactional
    @CacheEvict(value = "buscar-clientes", allEntries = true)
    public ClienteResponse criarCliente(ClienteRequest req, MultipartFile file)
    {
        log.info("Iniciando a criação de uma novo cliente.");
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
        pessoa.setOrganizacaoId(new Organizacao(UUID.fromString("bb1827b3-57b9-49ec-a775-a7f5a91b8297")));
        Pessoa pessoaSalva = pessoaRepository.save(pessoa);

        // 4. Cliente
        Cliente cliente = clienteMapper.toEntity(req);
        // idOrganização= bb1827b3-57b9-49ec-a775-a7f5a91b8297
        if (file != null && !file.isEmpty()) {
            final String filePath = fileService.saveFile(file, pessoa.getNome(), "Cliente");
            cliente.setArquivoPath(filePath);
        }
        cliente.setOrganizacaoId(new Organizacao(UUID.fromString("bb1827b3-57b9-49ec-a775-a7f5a91b8297")));
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
           @CacheEvict(value = "buscar-cliente-por-id", key = "#id")
    })
    public ClienteResponse alterarCliente(UUID id, ClienteRequest req, MultipartFile file)
    {
        log.info("Iniciando a atualização do cliente com ID: {}", id);

        Cliente clienteExistente = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));

        Pessoa pessoaExistente = clienteExistente.getPessoaId();
        Endereco endereco1Existente = pessoaExistente.getAdress1();
        Endereco endereco2Existente = pessoaExistente.getAdress2();

        // Atualizar Endereço 1
        Endereco endereco1NovosDados = enderecoMapper.toEntity(req.getCodPessoa().getCodAdress1());
        endereco1NovosDados.setId(endereco1Existente.getId());
        enderecoRepository.save(endereco1NovosDados);

        // Atualizar Endereço 2
        Endereco endereco2Salvo = null;
        if (req.getCodPessoa().getCodAdress2() != null) {
            Endereco endereco2NovosDados = enderecoMapper.toEntity(req.getCodPessoa().getCodAdress2());
            if (endereco2Existente != null) {
                endereco2NovosDados.setId(endereco2Existente.getId());
            }
            endereco2Salvo = enderecoRepository.save(endereco2NovosDados);
        } else if (endereco2Existente != null) {
            enderecoRepository.delete(endereco2Existente);
        }

        // Atualizar Pessoa
        Pessoa pessoaAtualizada = pessoaMapper.toEntity(req.getCodPessoa());
        pessoaAtualizada.setId(pessoaExistente.getId());
        pessoaAtualizada.setAdress1(endereco1Existente);
        pessoaAtualizada.setAdress2(endereco2Salvo);
        pessoaAtualizada.setOrganizacaoId(pessoaExistente.getOrganizacaoId());
        pessoaAtualizada = pessoaRepository.save(pessoaAtualizada);

        // Atualizar Cliente
        Cliente clienteAtualizado = clienteMapper.toEntity(req);
        if(file != null)
        {
          final String filePath = fileService.saveFile(file, pessoaExistente.getNome(), "Cliente");
          clienteAtualizado.setArquivoPath(filePath);
        }else{
          clienteAtualizado.setArquivoPath(clienteExistente.getArquivoPath());
        }
        clienteAtualizado.setOrganizacaoId(clienteExistente.getOrganizacaoId());
        clienteAtualizado.setId(clienteExistente.getId());
        clienteAtualizado.setPessoaId(pessoaAtualizada);

        Cliente clienteSalvo = repository.save(clienteAtualizado);

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
        repository.delete(cliente);
        log.info("Cliente com ID {} excluído com sucesso.", id);
    }

    // =========================================================================
    // READ - LIST (PAGINADO)
    // =========================================================================
    @Transactional(readOnly = true)
    public Page<ClienteResponse> listarClientes(Pageable pageable)
    {
        log.info("Iniciando a listagem de clientes.");
        return repository.findAll(pageable)
                .map(clienteMapper::toResponse);
    }

    // =========================================================================
    // READ - BY ID
    // =========================================================================
    @Transactional(readOnly = true)
    public ClienteResponse buscarClientePorId(UUID id) 
    {
        log.info("Iniciando a busca de cliente por ID {}.", id);
        Cliente cliente = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));
        log.info("Cliente com ID {} foi encontrado.", id);
        return clienteMapper.toResponse(cliente);
    }

}
