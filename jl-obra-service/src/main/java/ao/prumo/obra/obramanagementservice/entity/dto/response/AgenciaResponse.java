package ao.prumo.obra.obramanagementservice.entity.dto.response;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AgenciaResponse
{
    private UUID codAgencia;
    private String abreviacao;
    private String descricao;
    private String arquivoPath;
}
