package ao.prumo.obra.obramanagementservice.entity.dto.request;

import ao.prumo.obra.obramanagementservice.entity.enums.TipoPagamento;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EtapaObraRequest
{

    private Date dataCriacao;
    private Date dataEtapa;
    private Integer quantidade;
    private BigDecimal precoUnitario;
    private BigDecimal precoTotal;
    private Boolean status;
    private TipoPagamento tipoPagamento;
    private UUID codObra;
    private UUID codDespesa;
    private UUID codContaOrganizacao;
    private UUID codPagamentoProjecto;

}
