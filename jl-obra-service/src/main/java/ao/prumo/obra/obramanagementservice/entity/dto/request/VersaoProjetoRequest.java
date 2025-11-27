package ao.prumo.obra.obramanagementservice.entity.dto.request;

import ao.prumo.obra.obramanagementservice.entity.enums.Status;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VersaoProjetoRequest
{
    private String descricao;
    private String notaInformativa;
    private Date dataCriacao;
    private Boolean estado;

    private Status status;

    private UUID codProjetoArquitetonico;
    private UUID codContaOrganizacao;

}
