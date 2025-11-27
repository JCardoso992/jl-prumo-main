package ao.prumo.obra.obramanagementservice.entity.dto.response;

import ao.prumo.obra.obramanagementservice.entity.DocumentoProjeto;
import ao.prumo.obra.obramanagementservice.entity.enums.Formato;
import ao.prumo.obra.obramanagementservice.entity.enums.TipoDocumento;
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
public class DocumentoProjetoResponse extends BaseEntityResponse<DocumentoProjeto, DocumentoProjetoResponse>
{
    private UUID codDocumentoProjeto;
    private Date dataCriacao;
    private String arquivoPath;
    private String legenda;
    private TipoDocumento tipoDocumento;
    private Formato formato;
    private VersaoProjetoResponse codVersaoProjeto;
    private Boolean status;
    private FuncionarioResponse codArquitectoDesenho;

    @Override
    public DocumentoProjetoResponse convertToDTO(DocumentoProjeto entity) {
        DocumentoProjetoResponse resp = new DocumentoProjetoResponse();
        BeanUtils.copyProperties(entity, resp);
        resp.setCodDocumentoProjeto(entity.getId());
        if(entity.getVersaoProjetoId()!=null){
        resp.setCodVersaoProjeto(new VersaoProjetoResponse().convertToDTO(entity.getVersaoProjetoId()));}
        if (entity.getArquitectoDesenhoId()!=null){
            resp.setCodArquitectoDesenho(new FuncionarioResponse().convertToDTO(entity.getArquitectoDesenhoId()));}

        return resp;
    }

    @Override
    public List<DocumentoProjetoResponse> listToDTO(List<DocumentoProjeto> list) {
        return list.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public HashMap paginar(Page page) {
        return super.paginar(page);
    }
}
