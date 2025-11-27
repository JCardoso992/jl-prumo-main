package ao.prumo.obra.obramanagementservice.entity.dto.response;

import ao.prumo.obra.obramanagementservice.entity.Funcionario;
import ao.prumo.obra.obramanagementservice.entity.Obra;
import ao.prumo.obra.obramanagementservice.entity.VersaoProjeto;
import ao.prumo.obra.obramanagementservice.utils.base.BaseEntityResponse;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Component
public class ObraResponse  extends BaseEntityResponse<Obra, ObraResponse>
{
    private UUID codObra;
    private Date dataCriacao;
    private Boolean status;
    private FuncionarioResponse codEncarregado;
    private VersaoProjetoResponse codVersaoProjeto;

    @Override
    public ObraResponse convertToDTO(Obra entity) {
        ObraResponse resp = new ObraResponse();
        BeanUtils.copyProperties(entity, resp);
        resp.setCodObra(entity.getId());
        if (entity.getEncarregadoId()!=null){
        resp.setCodEncarregado(new FuncionarioResponse().convertToDTO(entity.getEncarregadoId()));}
        if (entity.getVersaoProjetoId()!=null){
        resp.setCodVersaoProjeto(new VersaoProjetoResponse().convertToDTO(entity.getVersaoProjetoId()));}
        return null;
    }

    @Override
    public List<ObraResponse> listToDTO(List<Obra> list) {
        return list.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public HashMap paginar(Page page) {
        return super.paginar(page);
    }
}
