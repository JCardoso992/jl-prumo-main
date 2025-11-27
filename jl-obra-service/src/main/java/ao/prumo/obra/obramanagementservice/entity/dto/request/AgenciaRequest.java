package ao.prumo.obra.obramanagementservice.entity.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AgenciaRequest
{

    @NotNull(message = "[abreviacao] é um campo é obrigatório.")
    @NotBlank(message = "[abreviacao], este campo não pode ficar em branco.")
    private String abreviacao;
    @NotNull(message = "[descricao] é um campo é obrigatório.")
    @NotBlank(message = "[descricao], este campo não pode ficar em branco.")
    private String descricao;
    private Boolean status;

}
