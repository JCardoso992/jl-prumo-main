package ao.prumo.obra.obramanagementservice.entity;

import ao.prumo.obra.obramanagementservice.entity.enums.Status;
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
@Table(name = "versao_projeto")
public class VersaoProjeto extends BaseAuditingEntity
{
    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;
    @Column(name = "descricao", columnDefinition = "TEXT")
    private String descricao;
    @Column(name = "nota_informativa")
    private String notaInformativa;
    @Column(name = "data_criacao")
    private Date dataCriacao;
    @Column(name = "estado")
    private Boolean estado;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY) // Lazy é melhor para performance
    @JoinColumn(name = "organizacao_id", referencedColumnName = "id")
    private Organizacao organizacaoId;
    @ManyToOne(fetch = FetchType.LAZY) // Lazy é melhor para performance
    @JoinColumn(name = "projeto_arquitetonico_id", referencedColumnName = "id")
    private ProjetoArquitetonico projetoArquitetonicoId;

    public VersaoProjeto(UUID codVersaoProjeto) {
        this.id = codVersaoProjeto;
    }
}