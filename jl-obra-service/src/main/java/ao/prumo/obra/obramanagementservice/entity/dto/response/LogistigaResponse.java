package ao.prumo.obra.obramanagementservice.entity.dto.response;

import ao.prumo.obra.obramanagementservice.entity.Logistica;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LogistigaResponse
{
    private UUID codLogistiga;
    private Date dataCriacao;
    private Integer quantiaTotal;
    private Integer quantiaDisponivel;
    private DespesaResponse codMercadoria;
}
