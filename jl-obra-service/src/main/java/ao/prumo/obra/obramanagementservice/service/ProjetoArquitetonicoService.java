package ao.prumo.obra.obramanagementservice.service;

import ao.prumo.obra.obramanagementservice.entity.Obra;
import ao.prumo.obra.obramanagementservice.entity.ProjetoArquitetonico;
import ao.prumo.obra.obramanagementservice.entity.repository.ProjetoArquitetonicoRepository;
import ao.prumo.obra.obramanagementservice.utils.base.BaseService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final ProjetoArquitetonicoMapper mapper;

    @Override
    protected JpaRepository<ProjetoArquitetonico, UUID> getRepository() {
        return this.repository;
    }

    // =========================================================================
    // CREATE
    // =========================================================================
    @Transactional
    public ProjetoArquitetonicoResponse criar(ProjetoArquitetonicoRequest request) 
    {
        log.info("Iniciando a criação de uma novo projeto arquitetônico");
        ProjetoArquitetonico entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }

    // =========================================================================
    // READ - LIST
    // =========================================================================
    @Transactional(readOnly = true)
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
    * @param req O DTO com os novos dados.
    * @return ProjetoArquitetonicoResponse com os dados atualizados.
    * @throws ResourceNotFoundException se o projeto arquitetônico não for encontrado.
    */
    @Transactional
    public ProjetoArquitetonicoResponse atualizar(UUID id, ProjetoArquitetonicoRequest request) 
    {
        log.info("Iniciando a atualização do projeto arquitetônico com ID {}.", id);
        ProjetoArquitetonico existente = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Projeto arquitetônico não encontrado"));

        ProjetoArquitetonico atualizado = mapper.toEntity(request);
        atualizado.setId(existente.getId());
        ProjetoArquitetonico atualizadoSalvo = repository.save(atualizado);
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
    public void excluir(UUID id) 
    {
        log.info("Iniciando a exclusão do projeto arquitetônico com ID {}.", id);
        ProjetoArquitetonico projeto = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Projeto arquitetônico não encontrado"));

        repository.delete(projeto);
        log.info("Projeto arquitetônico com ID {} removido com sucesso", id);
    }
}