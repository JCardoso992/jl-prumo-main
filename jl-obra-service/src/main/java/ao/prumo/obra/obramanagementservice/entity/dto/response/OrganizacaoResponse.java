package ao.prumo.obra.obramanagementservice.entity.dto.response;

import lombok.*;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrganizacaoResponse
{

    private UUID codOrganizacao;
    private String telefone1;
    private String telefone2;
    private String email1;
    private String email2;
    private String objecto;
    private String nif;
    private String numMatricula;
    private String firma;
    private String razaoSocial;
    private String arquivoPath;
    private String webPage;
    private String regime;
    private Boolean status;
    //KeyCloak
    private UUID keycloak_realm_id;

    private EnderecoResponse codAdress;
}
