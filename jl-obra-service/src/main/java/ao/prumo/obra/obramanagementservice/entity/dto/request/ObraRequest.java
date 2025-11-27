package ao.prumo.obra.obramanagementservice.entity.dto.request;

import lombok.*;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ObraRequest
{

    private Date dataCriacao;
    private Boolean status;
    private UUID codEncarregado;
    private UUID codVersaoProjeto;
    private UUID codOrganizacao;

}
