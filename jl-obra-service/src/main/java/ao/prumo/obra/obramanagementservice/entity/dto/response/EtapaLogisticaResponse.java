package ao.prumo.obra.obramanagementservice.entity.dto.response;

import ao.prumo.obra.obramanagementservice.entity.EtapaLogistica;
import ao.prumo.obra.obramanagementservice.entity.enums.LogisticaEstado;
import ao.prumo.obra.obramanagementservice.utils.base.BaseEntityResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Component
public class EtapaLogisticaResponse  extends BaseEntityResponse<EtapaLogistica, EtapaLogisticaResponse>
{
    private UUID codEtapaLogistica;
    private Date dataCriacao;
    private LogisticaEstado logisticaEstado;

    private EtapaObraResponse codEtapaObra;
    private LogistigaResponse codLogistiga;

    @Override
    public EtapaLogisticaResponse convertToDTO(EtapaLogistica entity) {
        EtapaLogisticaResponse resp = new EtapaLogisticaResponse();
        BeanUtils.copyProperties(entity, resp);
        resp.setCodEtapaLogistica(entity.getId());
        if (entity.getEtapaObraId()!=null)
        {
           resp.setCodEtapaObra(new EtapaObraResponse().convertToDTO(entity.getEtapaObraId()));
        }
        if (entity.getLogistigaId()!=null){
            resp.setCodLogistiga(new LogistigaResponse().convertToDTO(entity.getLogistigaId()));
        }
        return null;
    }

    @Override
    public List<EtapaLogisticaResponse> listToDTO(List<EtapaLogistica> list) {
        return list.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
}
