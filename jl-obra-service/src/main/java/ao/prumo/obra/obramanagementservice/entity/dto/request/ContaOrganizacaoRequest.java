package ao.prumo.obra.obramanagementservice.entity.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContaOrganizacaoRequest
{

    private UUID codContaOrganizacao;
    @NotNull(message = "Saldo é obrigatório")
    @DecimalMin(value = "0.0", inclusive = false, message = "Saldo deve ser maior que zero")
    @Digits(integer = 10, fraction = 2, message = "Saldo deve ter no máximo 2 casas decimais")
    private BigDecimal saldo;
    private BigDecimal saldoGasto;
    private BigDecimal saldoRemanescente;
    private UUID codAgencia;
    private Boolean status;

}
