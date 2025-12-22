package ao.prumo.obra.obramanagementservice.entity.dto.response;

import ao.prumo.obra.obramanagementservice.entity.enums.TipoPagamento;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EtapaObraResponse
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

}
