package ao.prumo.obra.obramanagementservice.entity.dto.response;

import ao.prumo.obra.obramanagementservice.entity.ContaOrganizacao;
import ao.prumo.obra.obramanagementservice.entity.PagamentoProjecto;
import ao.prumo.obra.obramanagementservice.entity.ProjetoArquitetonico;
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
public class PagamentoProjectoResponse extends BaseEntityResponse<PagamentoProjecto, PagamentoProjectoResponse>
{
    private UUID codPagamento;
    private Double ValorPago;
    private Double ValorRemanescente;
    private Double ValorUsado;
    private Date dataPagamento;
    private String descricao;
    private Boolean status;
    private ProjetoArquitetonicoResponse codProjetoArquitetonico;
    private ContaOrganizacaoResponse codContaOrganizacao;

    @Override
    public PagamentoProjectoResponse convertToDTO(PagamentoProjecto entity) {
        PagamentoProjectoResponse resp = new PagamentoProjectoResponse();
        BeanUtils.copyProperties(entity, resp);
        resp.setCodPagamento(entity.getId());
        if (entity.getContaOrganizacaoId()!=null){
        resp.setCodContaOrganizacao(new ContaOrganizacaoResponse().convertToDTO(entity.getContaOrganizacaoId()));}
        if (entity.getProjetoArquitetonicoId()!=null){
        resp.setCodProjetoArquitetonico(new ProjetoArquitetonicoResponse().convertToDTO(entity.getProjetoArquitetonicoId()));}
        return resp;
    }

    @Override
    public List<PagamentoProjectoResponse> listToDTO(List<PagamentoProjecto> list) {
        return list.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public HashMap paginar(Page page) {
        return super.paginar(page);
    }
}
