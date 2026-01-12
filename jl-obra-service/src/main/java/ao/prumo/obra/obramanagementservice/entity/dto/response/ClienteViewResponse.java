package ao.prumo.obra.obramanagementservice.entity.dto.response;

import ao.prumo.obra.obramanagementservice.entity.enums.EstadoCivil;
import ao.prumo.obra.obramanagementservice.entity.enums.Identificacao;
import ao.prumo.obra.obramanagementservice.entity.enums.Sexo;
import ao.prumo.obra.obramanagementservice.entity.enums.EstadoCliente;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;   
import java.util.Map;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClienteViewResponse 
{

    private UUID codCliente;

    private LocalDateTime createdDate;
    private String telefone1;
    private String telefone2;
    private String email;
    private String fax;
    private String arquivoPath;
    private String enderecoWeb;
    private Boolean status;
    private EstadoCliente estadoCliente;


    // Pessoa
    private UUID codPessoa;
    private String nome;
    private String nomePai;
    private String nomeMae;
    private String nif;
    private Double altura;
    private Date dateOfBird;
    private Date emitidoEm;
    private Date validoAte;

    private Sexo sexo;
    private EstadoCivil estadoCivil;
    private Identificacao documentoIdentificacao;

    // Endereço
    private String endereco;
    private String cidade;
    
}