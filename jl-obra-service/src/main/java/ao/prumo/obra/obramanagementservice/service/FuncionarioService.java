package ao.prumo.obra.obramanagementservice.service;

import ao.prumo.obra.obramanagementservice.entity.*;
import ao.prumo.obra.obramanagementservice.entity.dto.mapper.EnderecoMapper;
import ao.prumo.obra.obramanagementservice.entity.dto.mapper.FuncionarioMapper;
import ao.prumo.obra.obramanagementservice.entity.dto.mapper.PessoaMapper;
import ao.prumo.obra.obramanagementservice.entity.dto.request.EnderecoRequest;
import ao.prumo.obra.obramanagementservice.entity.dto.request.FuncionarioRequest;
import ao.prumo.obra.obramanagementservice.entity.dto.response.FuncionarioResponse;
import ao.prumo.obra.obramanagementservice.entity.repository.EnderecoRepository;
import ao.prumo.obra.obramanagementservice.entity.repository.FuncionarioRepository;
import ao.prumo.obra.obramanagementservice.entity.repository.PessoaRepository;
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
public class FuncionarioService
{

    private final FuncionarioRepository funcionarioRepository;
    private final PessoaRepository pessoaRepository;
    private final EnderecoRepository enderecoRepository;

    private FuncionarioMapper funcionarioMapper;
    private PessoaMapper pessoaMapper;
    private EnderecoMapper enderecoMapper;

    protected JpaRepository<Funcionario, UUID> getRepository() {
        return this.funcionarioRepository;
    }
    /*
     A anotação @Transactional garante que, se ocorrer um erro em qualquer uma das etapas
     (salvar endereço, pessoa ou funcionário), todas as operações anteriores são desfeitas
     (rollback), evitando dados inconsistentes no banco.
     Consideração:
     Mapeamento com MapStruct: A cópia de propriedades com BeanUtils.copyProperties funciona,
     mas é baseada em reflexão e pode ser lenta. Considere usar uma biblioteca como o MapStruct,
     que gera o código de mapeamento em tempo de compilação, sendo muito mais performático.
    * */
    @Transactional
    public FuncionarioResponse criarFuncionario(FuncionarioRequest request) 
    {
        log.info("Iniciando a criação de uma novo funcionario.");
        // 1. Endereço 1 (obrigatório)
        EnderecoRequest endRequest1 = request.getCodPessoa().getCodAdress1();
        Endereco endereco1 = enderecoMapper.toEntity(endRequest1);
        Endereco endereco1Salvo = enderecoRepository.save(endereco1);

         // 2. Endereço 2 (opcional)
        Endereco endereco2Salvo = null;
        if (request.getCodPessoa().getCodAdress2() != null) {
            EnderecoRequest endRequest2 = request.getCodPessoa().getCodAdress2();
            Endereco endereco2 = enderecoMapper.toEntity(endRequest2);
            endereco2Salvo = enderecoRepository.save(endereco2);
        }

        // 3. Pessoa
        Pessoa pessoa = pessoaMapper.toEntity(request.getCodPessoa());
        pessoa.setAdress1(endereco1Salvo);
        pessoa.setAdress2(endereco2Salvo);
        Pessoa pessoaSalva = pessoaRepository.save(pessoa);

        // 4. Funcionário
        Funcionario funcionario = funcionarioMapper.toEntity(request);
        funcionario.setPessoaId(pessoaSalva);
        Funcionario salvo = funcionarioRepository.save(funcionario);
        log.info("Funcionario criado com sucesso.");
        return funcionarioMapper.toResponse(salvo);
    }

    // =========================================================================
    // READ
    // =========================================================================
    @Transactional(readOnly = true)
    public FuncionarioResponse buscarPorId(UUID id) 
    {
        log.info("Iniciando a busca de funcionario por ID {}.", id);
        Funcionario funcionario = funcionarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Funcionário não encontrado"));
        log.info("Funcionario com ID {} foi encontrado.", id);
        return funcionarioMapper.toResponse(funcionario);
    }

    @Transactional(readOnly = true)
    public Page<FuncionarioResponse> listar(Pageable pageable)
    {
        log.info("Iniciando a listagem de funcionarios.");
        return funcionarioRepository.findAll(pageable)
                .map(funcionarioMapper::toResponse);
    }


   /**
     * Atualiza um funcionario existente buscando pelo ID.
     * @param id O UUID do funcionario a ser alterado.
     * @param req O DTO com os novos dados.
     * @return FuncionarioResponse com os dados atualizados.
     * @throws ResourceNotFoundException se o funcionario não for encontrada.
     */
    @Transactional
    public FuncionarioResponse alterarFuncionario(UUID id, FuncionarioRequest req) 
    {
         log.info("Iniciando a atualização do funcionario com ID: {}", id);
         Funcionario funcionarioExistente = funcionarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Funcionario não encontrado"));

        Pessoa pessoaExistente = funcionarioExistente.getPessoaId();
        Endereco endereco1Existente = funcionarioExistente.getPessoaId().getAdress1();
        Endereco endereco2Existente = funcionarioExistente.getPessoaId().getAdress2();

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
        pessoaAtualizada = pessoaRepository.save(pessoaAtualizada);

        // 4. Funcionário
        Funcionario funcionarioAtualizado = funcionarioMapper.toEntity(req);
        funcionarioAtualizado.setId(funcionarioExistente.getId());
        funcionarioAtualizado.setPessoaId(pessoaAtualizada);

        Funcionario funcionarioSalvo = funcionarioRepository.save(funcionarioAtualizado);

        log.info("Funcionario com ID {} atualizado com sucesso.", id);
        return funcionarioMapper.toResponse(funcionarioSalvo);
    }
    // =========================================================================
    // DELETE
    // =========================================================================
    /**
     * Elimina um funcionario pelo seu ID.
     * @param id O UUID do funcionario a ser excluído.
     * @throws ResourceNotFoundException se o funcionario não for encontrado.
     */
    @Transactional
    public void excluirFuncionario(UUID id) 
    {
        log.info("Iniciando a exclusão do funcionario com ID {}.", id);
        Funcionario funcionario = funcionarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Funcionário não encontrado"));
        funcionarioRepository.delete(funcionario);
        log.info("Funcionário com ID {} removido", id);
    }

}