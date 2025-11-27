package ao.prumo.obra.obramanagementservice.entity.dto.response;

import lombok.*;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CargoResponse
{
    private UUID codCargo;
    private String abreviacao;
    private String descricao;
    private Boolean status;

}
