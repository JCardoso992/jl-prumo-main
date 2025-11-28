package ao.prumo.obra.obramanagementservice.entity.dto.response;

import ao.prumo.obra.obramanagementservice.entity.enums.TipoPagamento;
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
@NoArgsConstructor
@AllArgsConstructorº
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
