package ao.prumo.obra.obramanagementservice.entity.dto.response;

import ao.prumo.obra.obramanagementservice.entity.EtapaObra;
import ao.prumo.obra.obramanagementservice.entity.PagamentoProjecto;
import ao.prumo.obra.obramanagementservice.entity.enums.TipoPagamento;
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
public class EtapaObraResponse extends BaseEntityResponse<EtapaObra, EtapaObraResponse>
{
    private UUID codEtapaObra;
    private Date dataCriacao;
    private Date dataEtapa;
    private Integer quantidade;
    private BigDecimal precoUnitario;
    private BigDecimal precoTotal;
    private Boolean status;
    private TipoPagamento tipoPagamento;
    private ObraResponse codObra;
    private DespesaResponse codDespesa;
    private ContaOrganizacaoResponse codContaOrganizacao;
    private PagamentoProjectoResponse codPagamentoProjecto;

    @Override
    public EtapaObraResponse convertToDTO(EtapaObra entity) {
        EtapaObraResponse resp = new EtapaObraResponse();
        BeanUtils.copyProperties(entity, resp);
        resp.setCodEtapaObra(entity.getId());
        if (entity.getObraId()!=null){
        resp.setCodObra(new ObraResponse().convertToDTO(entity.getObraId()));}
        if(entity.getDespesaId()!=null){
        resp.setCodDespesa(new DespesaResponse().convertToDTO(entity.getDespesaId()));}
        if(entity.getContaOrganizacaoId()!=null){
        resp.setCodContaOrganizacao(new ContaOrganizacaoResponse().convertToDTO(entity.getContaOrganizacaoId()));}
        if(entity.getPagamentoProjectoId()!=null){
            resp.setCodPagamentoProjecto(new PagamentoProjectoResponse().convertToDTO(entity.getPagamentoProjectoId()));}
        return resp;
    }

    @Override
    public List<EtapaObraResponse> listToDTO(List<EtapaObra> list) {
        return list.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

}
