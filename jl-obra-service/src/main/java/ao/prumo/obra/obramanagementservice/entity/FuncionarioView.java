package ao.prumo.obra.obramanagementservice.entity;

import ao.prumo.obra.obramanagementservice.entity.enums.TipoContrato;
import ao.prumo.obra.obramanagementservice.entity.enums.EstadoCivil;
import ao.prumo.obra.obramanagementservice.entity.enums.Identificacao;
import ao.prumo.obra.obramanagementservice.entity.enums.Sexo;
import ao.prumo.obra.obramanagementservice.entity.enums.EstadoUtilizador;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import java.math.BigDecimal;
import java.util.Map;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "vw_funcionario_detalhado")
@Immutable
@Getter
@Setter
public class FuncionarioView {

    @Id
    @Column(name = "funcionario_id")
    private UUID funcionarioId;

    private LocalDateTime createdDate;
    private String numProcesso;
    private String numConta;
    private String iban;
    private String telefone;
    private String email;
    private BigDecimal salario;
    private String arquivoPath;

    @Enumerated(EnumType.STRING)
    private EstadoUtilizador estadoUtilizador;
    @Enumerated(EnumType.STRING)
    private TipoContrato tipoContrato;

    @Column(name = "organizacao_id")
    private UUID organizacaoId;

    // Pessoa
    private UUID pessoaId;
    private String nome;
    private String nomePai;
    private String nomeMae;
    private String nif;
    private Double altura;
    private Date dateOfBird;
    private Date emitidoEm;
    private Date validoAte;

    @Enumerated(EnumType.STRING)
    private Sexo sexo;
    @Enumerated(EnumType.STRING)
    private EstadoCivil estadoCivil;
    @Enumerated(EnumType.STRING)
    private Identificacao documentoIdentificacao;

    // Endereço
    private String endereco;
    private String cidade;

    // Agência
    private UUID agenciaId;
    private String agenciaDescricao;
    private String agenciaAbrev;

    // Cargo
    private UUID cargoId;
    private String cargoDescricao;
    private String cargoAbrev;
}

