package ao.prumo.obra.obramanagementservice.entity;

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
@Table(name = "logistiga")
public class Logistica extends BaseAuditingEntity
{

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;
    @Column(name = "quantia_total")
    private Integer quantiaTotal;
    @Column(name = "quantia_disponivel")
    private Integer quantiaDisponivel;

    @ManyToOne(fetch = FetchType.LAZY) // Lazy é melhor para performance
    @JoinColumn(name = "mercadoria_id", referencedColumnName = "id")
    private Despesa mercadoriaId;
    @ManyToOne(fetch = FetchType.LAZY) // Lazy é melhor para performance
    @JoinColumn(name = "organizacao_id", referencedColumnName = "id")
    private Organizacao organizacaoId;

    public Logistica(UUID id) {
        this.id = id;
    }
}
