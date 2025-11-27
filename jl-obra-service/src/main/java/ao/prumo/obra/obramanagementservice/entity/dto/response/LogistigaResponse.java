package ao.prumo.obra.obramanagementservice.entity.dto.response;

import ao.prumo.obra.obramanagementservice.entity.Logistica;
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
public class LogistigaResponse  extends BaseEntityResponse<Logistica, LogistigaResponse>
{
    private UUID codLogistiga;
    private Date dataCriacao;
    private Integer quantiaTotal;
    private Integer quantiaDisponivel;

    private DespesaResponse codMercadoria;

    @Override
    public LogistigaResponse convertToDTO(Logistica entity) {
        LogistigaResponse  resp = new LogistigaResponse();
        BeanUtils.copyProperties(entity, resp);
        resp.setCodLogistiga(entity.getId());
        if(entity.getMercadoriaId()!=null){
            resp.setCodMercadoria(new DespesaResponse().convertToDTO(entity.getMercadoriaId()));}
        return null;
    }

    @Override
    public List<LogistigaResponse> listToDTO(List<Logistica> list) {
        return list.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

}
