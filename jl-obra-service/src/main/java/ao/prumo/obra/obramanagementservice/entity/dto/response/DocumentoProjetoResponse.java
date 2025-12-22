package ao.prumo.obra.obramanagementservice.entity.dto.response;

import ao.prumo.obra.obramanagementservice.entity.enums.Formato;
import ao.prumo.obra.obramanagementservice.entity.enums.TipoDocumento;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DocumentoProjetoResponse
{
    private UUID codDocumentoProjeto;
    private Date dataCriacao;
    private String arquivoPath;
    private String legenda;
    private TipoDocumento tipoDocumento;
    private Formato formato;
    private UUID codVersaoProjeto;
    private Boolean status;
    private FuncionarioResponse codArquitectoDesenho;

}
