package ao.prumo.obra.obramanagementservice.entity.dto.response;

import ao.prumo.obra.obramanagementservice.entity.VersaoProjeto;
import ao.prumo.obra.obramanagementservice.entity.enums.Status;
import ao.prumo.obra.obramanagementservice.utils.base.BaseEntityResponse;
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
public class VersaoProjetoResponse extends BaseEntityResponse<VersaoProjeto, VersaoProjetoResponse>
{
    private UUID codVersaoProjeto;
    private String descricao;
    private String notaInformativa;
    private Date dataCriacao;
    private Boolean estado;
    private Status status;
    private ProjetoArquitetonicoResponse codProjetoArquitetonico;

    @Override
    public VersaoProjetoResponse convertToDTO(VersaoProjeto entity) {
        VersaoProjetoResponse resp = new VersaoProjetoResponse();
        BeanUtils.copyProperties(entity, resp);
        resp.setCodVersaoProjeto(entity.getId());
        if (entity.getProjetoArquitetonicoId()!= null){
        resp.setCodProjetoArquitetonico(new ProjetoArquitetonicoResponse().convertToDTO(entity.getProjetoArquitetonicoId()));}
        return resp;
    }

    @Override
    public List<VersaoProjetoResponse> listToDTO(List<VersaoProjeto> list) {
        return list.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public HashMap paginar(Page page) {
        return super.paginar(page);
    }
}
