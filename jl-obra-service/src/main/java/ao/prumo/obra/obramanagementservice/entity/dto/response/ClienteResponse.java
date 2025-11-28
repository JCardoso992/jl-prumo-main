package ao.prumo.obra.obramanagementservice.entity.dto.response;

import ao.prumo.obra.obramanagementservice.entity.enums.EstadoCliente;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClienteResponse
{
    private UUID codCliente;
    private String telefone1;
    private String telefone2;
    private String email;
    private String fax;
    private String arquivoPath;
    private String enderecoWeb;
    private Boolean status;
    private EstadoCliente estadoCliente;
    private PessoaResponse codPessoa;
}
