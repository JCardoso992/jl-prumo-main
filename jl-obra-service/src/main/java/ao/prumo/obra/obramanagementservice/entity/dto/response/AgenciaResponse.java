package ao.prumo.obra.obramanagementservice.entity.dto.response;

import ao.prumo.obra.obramanagementservice.entity.Agencia;
import lombok.*;

import java.util.HashMap;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AgenciaResponse
{
    private UUID codAgencia;
    private String abreviacao;
    private String descricao;
    private Boolean status;
    private String arquivoPath;

}
