package ao.prumo.obra.obramanagementservice.entity.dto.response;

import ao.prumo.obra.obramanagementservice.entity.LogisticaPreco;
import ao.prumo.obra.obramanagementservice.utils.base.BaseEntityResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Component
public class LogisticaPrecoResponse  extends BaseEntityResponse<LogisticaPreco, LogisticaPrecoResponse>
{
    private UUID codLogisticaPreco;
    private BigDecimal preco;
    private Date dataCriacao;
    private LogistigaResponse codLogistiga;

    @Override
    public LogisticaPrecoResponse convertToDTO(LogisticaPreco entity) {
        LogisticaPrecoResponse resp = new LogisticaPrecoResponse();
        BeanUtils.copyProperties(entity, resp);
        resp.setCodLogisticaPreco(entity.getId());
        if(entity.getLogistigaId()!=null)
        {
            resp.setCodLogistiga(new LogistigaResponse().convertToDTO(entity.getLogistigaId()));
        }
        return resp;
    }

    @Override
    public List<LogisticaPrecoResponse> listToDTO(List<LogisticaPreco> list) {
        return list.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

}
