package ao.prumo.obra.obramanagementservice.entity.dto.response;

import ao.prumo.obra.obramanagementservice.entity.enums.TipoContrato;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FuncionarioResponse
{
    private UUID codFuncionario;
    private String numProcesso;
    private String numConta;
    private String iban;
    private String telefone;
    private String email;
    private BigDecimal salario;
    private Boolean status;
    private String arquivoPath;
    private TipoContrato tipoContrato;
    private AgenciaResponse codAgencia;
    private CargoResponse codCargo;
    private PessoaResponse codPessoa;

}
