package ao.prumo.obra.obramanagementservice.entity;

import ao.prumo.obra.obramanagementservice.entity.enums.EstadoCivil;
import ao.prumo.obra.obramanagementservice.entity.enums.Identificacao;
import ao.prumo.obra.obramanagementservice.entity.enums.Sexo;
import ao.prumo.obra.obramanagementservice.utils.base.BaseAuditingEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "pessoa")
public class Pessoa extends BaseAuditingEntity
{
    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;
    @Column(name = "nome", columnDefinition = "TEXT")
    private String nome;
    @Column(name = "nome_pai", columnDefinition = "TEXT")
    private String nomePai;
    @Column(name = "nome_mae", columnDefinition = "TEXT")
    private String nomeMae;
    @Column(name = "nif", columnDefinition = "TEXT", unique = true, nullable = false)
    private String nif;
    @Column(name = "altura")
    private Double altura;
    @Column(name = "status")
    private Boolean status;

    @Column(name = "date_of_bird")
    private Date dateOfBird;
    @Column(name = "emitido_em")
    private Date emitidoEm;
    @Column(name = "valido_ate")
    private Date validoAte;

    @Enumerated(EnumType.STRING)
    private Sexo sexo;
    @Enumerated(EnumType.STRING)
    private EstadoCivil estadoCivil;
    @Enumerated(EnumType.STRING)
    private Identificacao documentoIdentificacao;

    @OneToOne
    @JoinColumn(name = "adress1", referencedColumnName = "id")
    private Endereco adress1;
    @OneToOne
    @JoinColumn(name = "adress2", referencedColumnName = "id")
    private Endereco adress2;
    @ManyToOne
    @JoinColumn(name = "organizacao_id", referencedColumnName = "id")
    private Organizacao organizacaoId;

    public Pessoa(UUID codPessoa) {
        this.id = codPessoa;
    }
}
