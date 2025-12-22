package ao.prumo.obra.obramanagementservice.entity.dto.response;

import ao.prumo.obra.obramanagementservice.entity.enums.Status;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VersaoProjetoResponse
{
    private UUID codVersaoProjeto;
    private String descricao;
    private String notaInformativa;
    private Date dataCriacao;
    private Boolean estado;
    private Status status;
    private ProjetoArquitetonicoResponse codProjetoArquitetonico;

}
