package ao.prumo.obra.obramanagementservice.entity.dto.response;

import lombok.*;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DespesaResponse
{

    private UUID codDespesa;
    private String abreviacao;
    private String descricao;
    private DespesaResponse codDespesaPai;
    private Boolean status;

}
