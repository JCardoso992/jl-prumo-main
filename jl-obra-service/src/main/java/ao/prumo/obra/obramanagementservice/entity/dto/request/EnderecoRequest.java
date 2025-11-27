package ao.prumo.obra.obramanagementservice.entity.dto.request;

import lombok.*;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EnderecoRequest
{

    // Campos específicos do Endereco
    private UUID id;
    private String endereco;
    private String cidade;
    private String provincia;
    private String pais;
    private String codePlus;
    private Double latitude;
    private Double longitude;
    private Double altitude;
    private Boolean status;

}
