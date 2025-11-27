package ao.prumo.obra.obramanagementservice.entity.dto.response;

import lombok.*;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EnderecoResponse
{
    private UUID codEndereco;
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
