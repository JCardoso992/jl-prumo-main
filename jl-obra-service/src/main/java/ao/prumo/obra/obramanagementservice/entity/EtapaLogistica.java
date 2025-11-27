package ao.prumo.obra.obramanagementservice.entity;

import ao.prumo.obra.obramanagementservice.entity.enums.LogisticaEstado;
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
@Table(name = "etapa_logistica")
public class EtapaLogistica extends BaseAuditingEntity
{
    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;
    @Column(name = "data_criacao")
    private Date dataCriacao;

    @Enumerated(EnumType.STRING)
    private LogisticaEstado logisticaEstado;

    @ManyToOne
    @JoinColumn(name = "organizacao_id", referencedColumnName = "id")
    private Organizacao organizacaoId;
    @ManyToOne
    @JoinColumn(name = "etapa_obra_id", referencedColumnName = "id")
    private EtapaObra etapaObraId;
    @ManyToOne
    @JoinColumn(name = "logistiga_id", referencedColumnName = "id")
    private Logistica logistigaId;
}
