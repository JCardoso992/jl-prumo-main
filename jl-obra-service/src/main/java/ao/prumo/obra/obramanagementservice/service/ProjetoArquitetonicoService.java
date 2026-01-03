package ao.prumo.obra.obramanagementservice.service;

import ao.prumo.obra.obramanagementservice.entity.Organizacao;
import ao.prumo.obra.obramanagementservice.entity.Cliente;
import ao.prumo.obra.obramanagementservice.entity.Endereco;
import ao.prumo.obra.obramanagementservice.entity.Funcionario;
import ao.prumo.obra.obramanagementservice.entity.ProjetoArquitetonico;
import ao.prumo.obra.obramanagementservice.entity.dto.mapper.ProjetoArquitetonicoMapper;
import ao.prumo.obra.obramanagementservice.entity.dto.mapper.EnderecoMapper;
import ao.prumo.obra.obramanagementservice.entity.dto.request.ProjetoArquitetonicoRequest;
import ao.prumo.obra.obramanagementservice.entity.dto.request.EnderecoRequest;
import ao.prumo.obra.obramanagementservice.entity.dto.response.ProjetoArquitetonicoResponse;
import ao.prumo.obra.obramanagementservice.entity.repository.ProjetoArquitetonicoRepository;
import ao.prumo.obra.obramanagementservice.entity.repository.EnderecoRepository;
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

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProjetoArquitetonicoService
{

    private final ProjetoArquitetonicoRepository repository;
    private final EnderecoRepository enderecoRepository;

    private final ProjetoArquitetonicoMapper mapper;
    private final EnderecoMapper enderecoMapper;

    protected JpaRepository<ProjetoArquitetonico, UUID> getRepository()
    {
        return this.repository;
    }

    // =========================================================================
    // CREATE
    // =========================================================================
    @Transactional
    @CacheEvict(value = "buscar-projeto-arquitetônicos", allEntries = true)
    public ProjetoArquitetonicoResponse criar(ProjetoArquitetonicoRequest request)
    {
        log.info("Iniciando a criação de uma novo projeto arquitetônico");
        // 1. Endereço 1 (obrigatório)
        EnderecoRequest endRequest = request.getCodEndereco();
        Endereco enderecoSalvo = enderecoRepository.save(enderecoMapper.toEntity(endRequest));

        ProjetoArquitetonico entity = mapper.toEntity(request);
        entity.setStatus(Boolean.TRUE);
        // idOrganização= bb1827b3-57b9-49ec-a775-a7f5a91b8297
        entity.setOrganizacaoId(new Organizacao(UUID.fromString("bb1827b3-57b9-49ec-a775-a7f5a91b8297")));
        entity.setEnderecoId(enderecoSalvo);
        entity.setArquitectoChefeId(new Funcionario(request.getCodArquitectoChefe()));
        entity.setClienteId(new Cliente(request.getCodcliente()));

        ProjetoArquitetonico entitySalva = repository.save(entity);
        log.info("Projeto arquitetônico criado com sucesso.");
        return mapper.toResponse(entitySalva);
    }

    // =========================================================================
    // READ - LIST
    // =========================================================================
    @Transactional(readOnly = true)
    @Cacheable("buscar-projeto-arquitetônicos")
    public Page<ProjetoArquitetonicoResponse> listar(Pageable pageable)
    {
        log.info("Iniciando a listagem de projetos arquitetônicos.");
        return repository.findAll(pageable)
                .map(mapper::toResponse);
    }

    // =========================================================================
    // READ - BY ID
    // =========================================================================
    @Transactional(readOnly = true)
    @Cacheable("buscar-projeto-arquitetônico-por-id")
    public ProjetoArquitetonicoResponse buscarPorId(UUID id) 
    {
        log.info("Iniciando a busca de projeto arquitetônico por ID {}.", id);
        ProjetoArquitetonico projeto = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Projeto arquitetônico não encontrado"));
        log.info("Projeto arquitetônico com ID {} foi encontrado.", id);
        return mapper.toResponse(projeto);
    }

    // =========================================================================
    // UPDATE
    // =========================================================================
    /**
    * Atualiza projeto arquitetônico existente buscando pelo ID.
    * @param id O UUID do projeto arquitetônico a ser alterada.
    * @param request O DTO com os novos dados.
    * @return ProjetoArquitetonicoResponse com os dados atualizados.
    * @throws ResourceNotFoundException se o projeto arquitetônico não for encontrado.
    */
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "buscar-projeto-arquitetônicos", allEntries = true),
            @CacheEvict(value = "buscar-projeto-arquitetônico-por-id", key = "#id")
    })
    public ProjetoArquitetonicoResponse atualizar(UUID id, ProjetoArquitetonicoRequest request) 
    {
        log.info("Iniciando a atualização do projeto arquitetônico com ID {}.", id);
        ProjetoArquitetonico existente = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Projeto arquitetônico não encontrado"));

        Endereco enderecoExistente = enderecoRepository.findById(existente.getEnderecoId().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Projeto arquitetônico não encontrado"));        

        enderecoMapper.updateEntityFromDto(request.getCodEndereco(), enderecoExistente);
        enderecoRepository.save(enderecoExistente);

        mapper.updateEntityFromDto(request, existente);         
        ProjetoArquitetonico atualizadoSalvo = repository.save(existente);
        log.info("Projeto arquitetônico com ID {} alterada com sucesso.", id);
        return mapper.toResponse(atualizadoSalvo);
    }

    // =========================================================================
    // DELETE
    // =========================================================================
    /**
    * Elimina projeto arquitetônico pelo seu ID.
    * @param id O UUID do projeto arquitetônico a ser excluída.
    * @throws ResourceNotFoundException se o projeto arquitetônico não for encontrado.
    */
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "buscar-projeto-arquitetônicos", allEntries = true),
            @CacheEvict(value = "buscar-projeto-arquitetônico-por-id", key = "#id")
    })
    public void excluir(UUID id) 
    {
        log.info("Iniciando a exclusão do projeto arquitetônico com ID {}.", id);
        ProjetoArquitetonico projeto = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Projeto arquitetônico não encontrado"));

        repository.delete(projeto);
        log.info("Projeto arquitetônico com ID {} removido com sucesso", id);
    }
}