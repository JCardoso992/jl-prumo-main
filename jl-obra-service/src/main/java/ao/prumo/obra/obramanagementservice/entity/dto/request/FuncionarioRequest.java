package ao.prumo.obra.obramanagementservice.entity.dto.request;


import ao.prumo.obra.obramanagementservice.entity.enums.EstadoUtilizador;
import ao.prumo.obra.obramanagementservice.entity.enums.TipoContrato;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FuncionarioRequest
{

    // Campos específicos do Funcionário
    private String numProcesso;
    @NotNull(message = "[numConta] é um campo é obrigatório.")
    @NotBlank(message = "[numConta], este campo não pode ficar em branco.")
    private String numConta;
    @NotNull(message = "[iban] é um campo é obrigatório.")
    @NotBlank(message = "[iban], este campo não pode ficar em branco.")
    private String iban;
    @NotNull(message = "[telefone] é um campo é obrigatório.")
    @NotBlank(message = "[telefone], este campo não pode ficar em branco.")
    private String telefone;
    @NotNull(message = "[email] é um campo é obrigatório.")
    @NotBlank(message = "[email], este campo não pode ficar em branco.")
    private String email;
    private BigDecimal salario;
    private Boolean status;
    @NotNull(message = "[arquivoPath] é um campo é obrigatório.")
    @NotBlank(message = "[arquivoPath], este campo não pode ficar em branco.")
    private String arquivoPath;
    private TipoContrato tipoContrato;
    private EstadoUtilizador estadoUtilizador;

    // IDs para entidades que já devem existir
    private UUID codAgencia;
    private UUID codCargo;
    private UUID codOrganizacao;
    private UUID keycloak_id;

    // Objeto aninhado para criar uma nova Pessoa junto com o Funcionário
    @Valid // Adiciona validação em cascata para o objeto PessoaRequest
    @NotNull(message = "Os dados da pessoa são obrigatórios.")
    private PessoaRequest codPessoa;

}
