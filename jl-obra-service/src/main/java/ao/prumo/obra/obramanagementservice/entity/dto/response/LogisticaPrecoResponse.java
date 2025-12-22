package ao.prumo.obra.obramanagementservice.entity.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LogisticaPrecoResponse
{
    private UUID codLogisticaPreco;
    private BigDecimal preco;
    private Date dataCriacao;
    private LogisticaResponse codLogistiga;

}
