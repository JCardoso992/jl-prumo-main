package ao.prumo.obra.obramanagementservice.entity.dto.response;

import ao.prumo.obra.obramanagementservice.entity.EtapaLogistica;
import ao.prumo.obra.obramanagementservice.entity.enums.LogisticaEstado;
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
public class EtapaLogisticaResponse
{
    private UUID codEtapaLogistica;
    private Date dataCriacao;
    private LogisticaEstado logisticaEstado;

    private EtapaObraResponse codEtapaObra;
    private UUID codLogistiga;

}
