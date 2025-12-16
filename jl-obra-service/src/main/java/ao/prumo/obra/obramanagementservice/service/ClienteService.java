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
public class ClienteService extends BaseService<Cliente, UUID>
{

    private final ClienteRepository repository;
    private final PessoaRepository pessoaRepository;
    private final EnderecoRepository enderecoRepository;

    private final ClienteMapper clienteMapper;

    private final PessoaMapper pessoaMapper;

    private final EnderecoMapper enderecoMapper;

    protected JpaRepository<Cliente, UUID> getRepository() {
        return this.repository;
    }

    @Transactional
    public ClienteResponse criarCliente(ClienteRequest req)
    {
        log.info("Iniciando a criação de uma novo cliente.");
        // Mapper converte o DTO para Entidade -> Service salva a Entidade
        // -> Mapper converte a Entidade salva para DTO de Resposta
        // 1. Salvar os Endereços
        EnderecoRequest endRequest1, endRequest2;
        endRequest1 = req.getCodPessoa().getCodAdress1();
        Endereco endereco1 = enderecoMapper.toEntity(endRequest1);
        Endereco endereco1Salvo = enderecoRepository.save(endereco1);

        Endereco endereco2Salvo = null;
        if (req.getCodPessoa().getCodAdress2() != null) {
            endRequest2 = req.getCodPessoa().getCodAdress2();
            Endereco endereco2 = enderecoMapper.toEntity(endRequest2);
            endereco2Salvo = enderecoRepository.save(endereco2);
        }

        // 2. Salvar a Pessoa
        Pessoa pessoa = pessoaMapper.toEntity(req.getCodPessoa());
        pessoa.setAdress1(endereco1Salvo);
        pessoa.setAdress2(endereco2Salvo);
        Pessoa pessoaSalva = pessoaRepository.save(pessoa);

        // 3. Salvar o Funcionário
        Cliente cliente = clienteMapper.toEntity(req);
        cliente.setPessoaId(pessoaSalva); // Associa a pessoa recém-criada
        return clienteMapper.toResponse(repository.save(cliente));
    }

    /**
     * Atualiza um cliente existente buscando pelo ID.
     * @param id O UUID do cliente a ser alterado.
     * @param req O DTO com os novos dados.
     * @return ClienteResponse com os dados atualizados.
     * @throws EntityNotFoundException se o cliente não for encontrada.
     */
    @Transactional
    public ClienteResponse alterarCliente(UUID id, ClienteRequest req) {
        log.info("Iniciando a atualização do cliente com ID: {}", id);

        // 1. Buscar o cliente existente (ou lançar erro se não encontrar)
        Cliente clienteExistente = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente com ID " + id + " não encontrado."));

        // 2. Recuperar as entidades vinculadas (Pessoa e Endereços)
        Pessoa pessoaExistente = clienteExistente.getPessoaId();
        Endereco endereco1Existente = pessoaExistente.getAdress1();
        Endereco endereco2Existente = pessoaExistente.getAdress2();

        // 3. Atualizar Endereço 1
        if (req.getCodPessoa().getCodAdress1() != null) {
            // Mapeia novos dados para a entidade existente para manter o ID e campos de auditoria
            Endereco endereco1DadosNovos = enderecoMapper.toEntity(req.getCodPessoa().getCodAdress1());
            // Aqui você pode usar BeanUtils ou setar manualmente os campos no endereco1Existente
            // Exemplo: endereco1Existente.setRua(endereco1DadosNovos.getRua());
            // Para simplificar, vamos salvar a conversão garantindo o ID original:
            endereco1DadosNovos.setId(endereco1Existente.getId());
            enderecoRepository.save(endereco1DadosNovos);
        }

        // 4. Atualizar Endereço 2 (Tratar criação, atualização ou remoção)
        if (req.getCodPessoa().getCodAdress2() != null) {
            Endereco endereco2DadosNovos = enderecoMapper.toEntity(req.getCodPessoa().getCodAdress2());
            if (endereco2Existente != null) {
                endereco2DadosNovos.setId(endereco2Existente.getId());
            }
            endereco2Existente = enderecoRepository.save(endereco2DadosNovos);
        } else if (endereco2Existente != null) {
            // Caso o request venha sem o segundo endereço mas ele existia antes (opcional: deletar ou desvincular)
            pessoaExistente.setAdress2(null);
            enderecoRepository.delete(endereco2Existente);
            endereco2Existente = null;
        }

        // 5. Atualizar Pessoa
        Pessoa pessoaDadosNovos = pessoaMapper.toEntity(req.getCodPessoa());
        pessoaDadosNovos.setId(pessoaExistente.getId());
        pessoaDadosNovos.setAdress1(endereco1Existente);
        pessoaDadosNovos.setAdress2(endereco2Existente);
        Pessoa pessoaAtualizada = pessoaRepository.save(pessoaDadosNovos);

        // 6. Atualizar Cliente
        Cliente clienteDadosNovos = clienteMapper.toEntity(req);
        clienteDadosNovos.setId(clienteExistente.getId());
        clienteDadosNovos.setPessoaId(pessoaAtualizada);

        Cliente clienteSalvo = repository.save(clienteDadosNovos);

        log.info("Cliente com ID {} atualizado com sucesso.", id);
        return clienteMapper.toResponse(clienteSalvo);
    }

    /**
     * Elimina um cliente pelo seu ID.
     * @param id O UUID do cliente a ser excluído.
     * @throws EntityNotFoundException se a cliente não for encontrado.
     */
    @Transactional
    public void excluirCliente(UUID id) throws EntityNotFoundException {
        // 1. Verificar se o cliente existe.
        // O método findById() é herdado do BaseService e já lança EntityNotFoundException se não encontrar.
        this.repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente Não Existe"));
        // 2. Se o cliente foi encontrada, prosseguir com a exclusão.
        this.repository.deleteById(id);

        log.info("Cliente com ID {} excluída com sucesso.", id);
    }

}
