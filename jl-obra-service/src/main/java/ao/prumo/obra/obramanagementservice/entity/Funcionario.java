package ao.prumo.obra.obramanagementservice.entity;


import ao.prumo.obra.obramanagementservice.entity.enums.EstadoUtilizador;
import ao.prumo.obra.obramanagementservice.entity.enums.TipoContrato;
import ao.prumo.obra.obramanagementservice.utils.base.BaseAuditingEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "funcionario")
public class Funcionario extends BaseAuditingEntity
{
    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;
    @Column(name = "num_processo", columnDefinition = "TEXT", unique = true, nullable = false)
    private String numProcesso;
    @Column(name = "num_conta", columnDefinition = "TEXT", unique = true, nullable = false)
    private String numConta;
    @Column(name = "iban", columnDefinition = "TEXT", unique = true, nullable = false)
    private String iban;
    @Column(name = "telefone", columnDefinition = "TEXT", unique = true, nullable = false)
    private String telefone;
    @Column(name = "email", columnDefinition = "TEXT", unique = true, nullable = false)
    private String email;
    @Column(name = "salario")
    private BigDecimal salario;
    @Column(name = "status")
    private Boolean status;
    @Column(name = "arquivo_path", columnDefinition = "TEXT")
    private String arquivoPath;

    @Enumerated(EnumType.STRING)
    private TipoContrato tipoContrato;
    @Enumerated(EnumType.STRING)
    private EstadoUtilizador estadoUtilizador;
    /*
    * FetchType.EAGER : Isso pode causar problemas de performance (N+1),
    * pois carrega todos os dados relacionados mesmo quando não são necessários.
    * FetchType.LAZY : melhor opção
    * */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agencia_id", referencedColumnName = "id")
    private Agencia agenciaId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cargo_id", referencedColumnName = "id")
    private Cargo cargoId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizacao_id", referencedColumnName = "id")
    private Organizacao organizacaoId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pessoa_id", referencedColumnName = "id")
    private Pessoa pessoaId;

    //KeyCloak
    private UUID keycloak_id;

    public Funcionario(UUID codFuncionario) {
        this.id = codFuncionario;
    }
}
