package ao.prumo.obra.obramanagementservice.entity;

import ao.prumo.obra.obramanagementservice.utils.base.BaseAuditingEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "conta_organizacao")
public class ContaOrganizacao extends BaseAuditingEntity
{

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;
    @Column(name = "saldo")
    private BigDecimal saldo;
    @Column(name = "saldo_gasto")
    private BigDecimal saldoGasto;
    @Column(name = "saldo_remanescente")
    private BigDecimal saldoRemanescente;
    @Column(name = "status")
    private Boolean status;

    @ManyToOne(fetch = FetchType.LAZY) // Lazy é melhor para performance
    @JoinColumn(name = "agencia_id", referencedColumnName = "id")
    private Agencia agenciaId;
    @ManyToOne(fetch = FetchType.LAZY) // Lazy é melhor para performance
    @JoinColumn(name = "organizacao_id", referencedColumnName = "id")
    private Organizacao organicacaoId;

    public ContaOrganizacao(UUID codContaOrganizacao) {
        this.id = codContaOrganizacao;
    }
}
