package ao.prumo.obra.obramanagementservice.entity.dto.request;

import lombok.*;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjetoArquitetonicoRequest
{

    private String codigoProcesso;
    private String notaInformativa;
    private String descricao;
    private Date dataInicio;
    private Date dataTermino;
    private Double orcamentoTotal;
    private Double saldoCaixa;
    private Boolean status;
    private UUID codcliente;
    private UUID codEndereco;
    private UUID codArquitectoChefe;

}
