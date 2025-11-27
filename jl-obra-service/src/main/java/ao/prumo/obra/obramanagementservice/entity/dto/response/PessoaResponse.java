package ao.prumo.obra.obramanagementservice.entity.dto.response;

import ao.prumo.obra.obramanagementservice.entity.enums.EstadoCivil;
import ao.prumo.obra.obramanagementservice.entity.enums.Identificacao;
import ao.prumo.obra.obramanagementservice.entity.enums.Sexo;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PessoaResponse
{
    private UUID codPessoa;
    private String nome;
    private String nomePai;
    private String nomeMae;
    private String nif;
    private Double altura;
    private Boolean status;
    private Date dateOfBird;
    private Date emitidoEm;
    private Date validoAte;
    private Sexo sexo;
    private EstadoCivil estadoCivil;
    private Identificacao documentoIdentificacao;
    // IDs para entidades que já devem existir
    private UUID codOrganizacao;

    private EnderecoResponse codAdress1;
    private EnderecoResponse codAdress2;

}
