package ao.prumo.obra.obramanagementservice.entity.dto.response;

import ao.prumo.obra.obramanagementservice.entity.DocumentoProjeto;
import ao.prumo.obra.obramanagementservice.entity.enums.Formato;
import ao.prumo.obra.obramanagementservice.entity.enums.TipoDocumento;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;
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
