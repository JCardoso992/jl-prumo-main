package ao.prumo.obra.obramanagementservice.entity.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DespesaRequest
{

    @NotNull(message = "[abreviacao] é um campo é obrigatório.")
    @NotBlank(message = "[abreviacao], este campo não pode ficar em branco.")
    private String abreviacao;
    @NotNull(message = "[descricao] é um campo é obrigatório.")
    @NotBlank(message = "[descricao], este campo não pode ficar em branco.")
    private String descricao;
    private UUID codDespesaPai;
    private Boolean status;
    private UUID codOrganizacao;

}
