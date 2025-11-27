package ao.prumo.obra.obramanagementservice.entity.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContaOrganizacaoResponse
{
    private UUID codContaOrganizacao;
    private BigDecimal saldo;
    private BigDecimal saldoGasto;
    private BigDecimal saldoRemanescente;
    private AgenciaResponse codAgencia;
    private Boolean status;

}
