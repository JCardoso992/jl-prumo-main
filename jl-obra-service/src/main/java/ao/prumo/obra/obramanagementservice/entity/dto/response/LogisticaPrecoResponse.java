package ao.prumo.obra.obramanagementservice.entity.dto.response;

import ao.prumo.obra.obramanagementservice.entity.LogisticaPreco;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LogisticaPrecoResponse
{
    private UUID codLogisticaPreco;
    private BigDecimal preco;
    private Date dataCriacao;
    private LogistigaResponse codLogistiga;

}
