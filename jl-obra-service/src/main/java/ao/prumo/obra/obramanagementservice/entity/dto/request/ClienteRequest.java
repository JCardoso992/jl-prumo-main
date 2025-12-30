package ao.prumo.obra.obramanagementservice.entity.dto.request;

import ao.prumo.obra.obramanagementservice.entity.Cliente;
import ao.prumo.obra.obramanagementservice.entity.Organizacao;
import ao.prumo.obra.obramanagementservice.entity.Pessoa;
import ao.prumo.obra.obramanagementservice.entity.dto.response.OrganizacaoResponse;
import ao.prumo.obra.obramanagementservice.entity.dto.response.PessoaResponse;
import ao.prumo.obra.obramanagementservice.entity.enums.EstadoCliente;
import ao.prumo.obra.obramanagementservice.utils.base.BaseEntityRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClienteRequest
{

    @NotNull(message = "Numero de telefone é um campo é obrigatório.")
    @NotBlank(message = "Numero de, este campo não pode ficar em branco.")
    private String telefone1;
    private String telefone2;
    private String email;
    private String fax;
    private String enderecoWeb;
    private EstadoCliente estadoCliente;

    // Objeto aninhado para criar uma nova Pessoa junto com o Funcionário
    @Valid // Adiciona validação em cascata para o objeto PessoaRequest
    @NotNull(message = "Os dados da pessoa são obrigatórios.")
    private PessoaRequest codPessoa;
    //@Future(message = "A data prevista de término deve ser futura")
    //@DecimalMin(value = "0.0", inclusive = false, message = "O orçamento deve ser maior que zero")

}
