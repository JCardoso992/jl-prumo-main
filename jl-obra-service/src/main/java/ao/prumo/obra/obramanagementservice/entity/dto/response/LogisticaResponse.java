package ao.prumo.obra.obramanagementservice.entity.dto.response;

import lombok.*;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LogisticaResponse
{
    private UUID codLogistiga;
    private Date dataCriacao;
    private Integer quantiaTotal;
    private Integer quantiaDisponivel;
    private DespesaResponse codMercadoria;
}
