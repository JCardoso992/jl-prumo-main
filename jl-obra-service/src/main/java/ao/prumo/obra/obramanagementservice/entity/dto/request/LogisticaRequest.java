package ao.prumo.obra.obramanagementservice.entity.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class LogisticaRequest
{
    private Integer quantiaTotal;
    private Integer quantiaDisponivel;
    private UUID codMercadoria;

}