package ao.prumo.obra.obramanagementservice.entity.dto.response;

import ao.prumo.obra.obramanagementservice.entity.Funcionario;
import ao.prumo.obra.obramanagementservice.entity.VersaoProjeto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ObraResponse
{
    private UUID codObra;
    private Date dataCriacao;
    private Boolean status;
    private FuncionarioResponse codEncarregado;
    private VersaoProjetoResponse codVersaoProjeto;

}
