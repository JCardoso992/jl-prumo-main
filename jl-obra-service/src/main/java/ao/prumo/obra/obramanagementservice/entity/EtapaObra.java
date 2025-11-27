package ao.prumo.obra.obramanagementservice.entity;

import ao.prumo.obra.obramanagementservice.entity.enums.TipoPagamento;
import ao.prumo.obra.obramanagementservice.utils.base.BaseAuditingEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "etapa_obra")
public class EtapaObra extends BaseAuditingEntity
{
    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;
    @Column(name = "data_criacao")
    private Date dataCriacao;
    @Column(name = "data_etapa")
    private Date dataEtapa;
    @Column(name = "quantidade")
    private Integer quantidade;
    @Column(name = "preco_unitario")
    private BigDecimal precoUnitario;
    @Column(name = "preco_total")
    private BigDecimal precoTotal;

    @Enumerated(EnumType.STRING)
    private TipoPagamento tipoPagamento;

    @ManyToOne
    @JoinColumn(name = "organizacao_id", referencedColumnName = "id")
    private Organizacao organizacaoId;
    @ManyToOne
    @JoinColumn(name = "obra_id", referencedColumnName = "id")
    private Obra obraId;
    @ManyToOne
    @JoinColumn(name = "despesa_id", referencedColumnName = "id")
    private Despesa despesaId;
    @ManyToOne
    @JoinColumn(name = "conta_organizacao_id", referencedColumnName = "id")
    private ContaOrganizacao contaOrganizacaoId;
    @ManyToOne
    @JoinColumn(name = "pagamento_projecto_id", referencedColumnName = "id")
    private PagamentoProjecto pagamentoProjectoId;

}
