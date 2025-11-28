package ao.prumo.obra.obramanagementservice.service;

import ao.prumo.obra.obramanagementservice.entity.*;
import ao.prumo.obra.obramanagementservice.entity.dto.mapper.EnderecoMapper;
import ao.prumo.obra.obramanagementservice.entity.dto.mapper.FuncionarioMapper;
import ao.prumo.obra.obramanagementservice.entity.dto.mapper.PessoaMapper;
import ao.prumo.obra.obramanagementservice.entity.dto.request.EnderecoRequest;
import ao.prumo.obra.obramanagementservice.entity.dto.request.FuncionarioRequest;
import ao.prumo.obra.obramanagementservice.entity.dto.request.PessoaRequest;
import ao.prumo.obra.obramanagementservice.entity.dto.response.FuncionarioResponse;
import ao.prumo.obra.obramanagementservice.entity.repository.EnderecoRepository;
import ao.prumo.obra.obramanagementservice.entity.repository.FuncionarioRepository;
import ao.prumo.obra.obramanagementservice.entity.repository.PessoaRepository;
import ao.prumo.obra.obramanagementservice.utils.base.BaseService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Getter
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class FuncionarioService extends BaseService<Funcionario, UUID> {

    private final FuncionarioRepository funcionarioRepository;
    private final PessoaRepository pessoaRepository;
    private final EnderecoRepository enderecoRepository;

    @Autowired
    private FuncionarioMapper funcionarioMapper;

    @Autowired
    private PessoaMapper pessoaMapper;

    @Autowired
    private EnderecoMapper enderecoMapper;

    protected JpaRepository<Funcionario, UUID> getRepository() {
        return this.repository;
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
    public FuncionarioResponse criarFuncionarioCompleto(FuncionarioRequest request) {
        // 1. Salvar os Endereços
        EnderecoRequest endRequest1 = request.getCodPessoa().getCodAdress1();
        Endereco endereco1 = enderecoMapper.toEntity(endRequest1);
        Endereco endereco1Salvo = enderecoRepository.save(endereco1);

        Endereco endereco2Salvo = null;
        if (request.getCodPessoa().getCodAdress2() != null) {
            EnderecoRequest endRequest2 = request.getCodPessoa().getCodAdress2();
            Endereco endereco2 = enderecoMapper.toEntity(endRequest2);
            endereco2Salvo = enderecoRepository.save(endereco2);
        }

        // 2. Salvar a Pessoa
        Pessoa pessoa = pessoaMapper.toEntity(request.getCodPessoa());
        pessoa.setAdress1(endereco1Salvo);
        pessoa.setAdress2(endereco2Salvo);
        Pessoa pessoaSalva = pessoaRepository.save(pessoa);

        // 3. Salvar o Funcionário
        //Funcionario funcionario = funcionarioMapper.toEntity(request);
        //funcionario.setPessoaId(pessoaSalva); // Associa a pessoa recém-criada
        // FuncionarioResponse response = funcionarioMapper.toResponse(repository.save(funcionario));
        FuncionarioResponse response = new FuncionarioResponse();
        return response;
    }

}