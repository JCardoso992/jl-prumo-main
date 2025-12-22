package ao.prumo.obra.obramanagementservice.entity.dto.response;

import lombok.*;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjetoArquitetonicoResponse
{
    private UUID codProjetoArquitetonico;
    private String codigoProcesso;
    private String notaInformativa;
    private String descricao;
    private Date dataInicio;
    private Date dataTermino;
    private Double orcamentoTotal;
    private Double saldoCaixa;
    private ClienteResponse codCliente;
    private EnderecoResponse codEndereco;
    private FuncionarioResponse codArquitectoChefe;
    private Boolean status;
    
}
