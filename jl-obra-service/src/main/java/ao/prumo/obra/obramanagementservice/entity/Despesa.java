package ao.prumo.obra.obramanagementservice.entity;

import ao.prumo.obra.obramanagementservice.utils.base.BaseAuditingEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "despesa")
public class Despesa extends BaseAuditingEntity
{
    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @Column(name = "abreviacao", columnDefinition = "TEXT")
    private String abreviacao;
    @Column(name = "descricao", columnDefinition = "TEXT")
    private String descricao;
    @Column(name = "status")
    private Boolean status;

    @ManyToOne(fetch = FetchType.LAZY) // Lazy é melhor para performance
    @JoinColumn(name = "organizacao_id", referencedColumnName = "id")
    private Organizacao organizacaoId;
    @ManyToOne(fetch = FetchType.LAZY) // Lazy é melhor para performance
    @JoinColumn(name = "despesa_pai_id", referencedColumnName = "id")
    private Despesa despesaPaiId;

    public Despesa(UUID codDespesa) {
        this.id = codDespesa;
    }
}