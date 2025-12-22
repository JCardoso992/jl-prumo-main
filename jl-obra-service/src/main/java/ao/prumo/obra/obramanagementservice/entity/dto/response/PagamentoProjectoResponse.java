package ao.prumo.obra.obramanagementservice.entity.dto.response;

import lombok.*;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PagamentoProjectoResponse
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
    
}
