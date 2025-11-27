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
@Table(name = "cargo")
public class Cargo extends BaseAuditingEntity
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

    @ManyToOne
    @JoinColumn(name = "organizacao_id", referencedColumnName = "id")
    private Organizacao organizacaoId;

    public Cargo(UUID codCargo) {
        this.id = codCargo;
    }
}
