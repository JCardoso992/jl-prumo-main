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
@Table(name = "obra")
public class Obra extends BaseAuditingEntity
{
    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @Column(name = "data_criacao")
    private Date dataCriacao;
    @Column(name = "status")
    private Boolean status;

    @ManyToOne(fetch = FetchType.LAZY) // Lazy é melhor para performance
    @JoinColumn(name = "encarregado_id", referencedColumnName = "id")
    private Funcionario encarregadoId;
    @ManyToOne(fetch = FetchType.LAZY) // Lazy é melhor para performance
    @JoinColumn(name = "versao_projeto_id", referencedColumnName = "id")
    private VersaoProjeto versaoProjetoId;
    @ManyToOne(fetch = FetchType.LAZY) // Lazy é melhor para performance
    @JoinColumn(name = "organizacao_id", referencedColumnName = "id")
    private Organizacao organizacaoId;

    public Obra(UUID codObra) {
        this.id = codObra;
    }
}
