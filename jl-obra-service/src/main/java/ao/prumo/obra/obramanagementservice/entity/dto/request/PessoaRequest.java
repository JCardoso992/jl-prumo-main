package ao.prumo.obra.obramanagementservice.entity.dto.request;

import ao.prumo.obra.obramanagementservice.entity.enums.EstadoCivil;
import ao.prumo.obra.obramanagementservice.entity.enums.Identificacao;
import ao.prumo.obra.obramanagementservice.entity.enums.Sexo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PessoaRequest
{
    // Campos específicos da Pessoa
    @NotNull(message = "[nome] é um campo é obrigatório.")
    @NotBlank(message = "[nome], este campo não pode ficar em branco.")
    private String nome;
    private String nomePai;
    private String nomeMae;
    @NotNull(message = "[nif] é um campo é obrigatório.")
    @NotBlank(message = "[nif], este campo não pode ficar em branco.")
    private String nif;
    private Double altura;
    @NotNull(message = "[dateOfBird], este campo não pode ficar em branco.")
    private Date dateOfBird;
    @NotNull(message = "[emitidoEm], este campo não pode ficar em branco.")
    private Date emitidoEm;
    @NotNull(message = "[validoAte], este campo não pode ficar em branco.")
    private Date validoAte;

    @NotNull(message = "[sexo], este campo não pode ficar em branco.")
    private Sexo sexo;
    @NotNull(message = "[estadoCivil], este campo não pode ficar em branco.")
    private EstadoCivil estadoCivil;
    @NotNull(message = "[documentoIdentificacao], este campo não pode ficar em branco.")
    private Identificacao documentoIdentificacao;

    // Objeto aninhado para criar uma nova Pessoa junto com o Funcionário
    @Valid // Adiciona validação em cascata para o objeto PessoaRequest
    @NotNull(message = "Os dados do endereço são obrigatórios.")
    private EnderecoRequest codAdress1;

    @Valid
    private EnderecoRequest codAdress2;
}
