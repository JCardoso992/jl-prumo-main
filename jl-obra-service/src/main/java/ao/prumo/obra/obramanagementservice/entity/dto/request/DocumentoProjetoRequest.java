package ao.prumo.obra.obramanagementservice.entity.dto.request;

import ao.prumo.obra.obramanagementservice.entity.enums.Formato;
import ao.prumo.obra.obramanagementservice.entity.enums.TipoDocumento;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DocumentoProjetoRequest
{

    private Date dataCriacao;
    @NotNull(message = "[ficheiro] é um campo é obrigatório.")
    @NotBlank(message = "[ficheiro], este campo não pode ficar em branco.")
    private String arquivoPath;
    @NotNull(message = "[legenda] é um campo é obrigatório.")
    @NotBlank(message = "[legenda], este campo não pode ficar em branco.")
    private String legenda;
    private TipoDocumento tipoDocumento;
    private Formato formato;
    private UUID codVersaoProjeto;
    private Boolean status;
    private UUID codArquitectoDesenho;

}
