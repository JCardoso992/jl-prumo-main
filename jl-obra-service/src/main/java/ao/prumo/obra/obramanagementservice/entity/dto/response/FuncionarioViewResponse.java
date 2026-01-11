package ao.prumo.obra.obramanagementservice.entity.dto.response;

import ao.prumo.obra.obramanagementservice.entity.enums.TipoContrato;
import ao.prumo.obra.obramanagementservice.entity.enums.EstadoCivil;
import ao.prumo.obra.obramanagementservice.entity.enums.Identificacao;
import ao.prumo.obra.obramanagementservice.entity.enums.Sexo;
import ao.prumo.obra.obramanagementservice.entity.enums.EstadoUtilizador;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;   
import java.math.BigDecimal;
import java.util.Map;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FuncionarioViewResponse {

    private UUID codFuncionario;

    private LocalDateTime createdDate;
    private String numProcesso;
    private String numConta;
    private String iban;
    private String telefone;
    private String email;
    private BigDecimal salario;
    private String arquivoPath;

    private TipoContrato tipoContrato;

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

    // Agência
    private UUID codAgencia;
    private String agenciaDescricao;
    private String agenciaAbrev;

    // Cargo
    private UUID codCargo;
    private String cargoDescricao;
    private String cargoAbrev;
}