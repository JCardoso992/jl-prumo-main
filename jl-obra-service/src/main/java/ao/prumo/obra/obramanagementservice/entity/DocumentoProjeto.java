package ao.prumo.obra.obramanagementservice.entity;

import ao.prumo.obra.obramanagementservice.entity.enums.Formato;
import ao.prumo.obra.obramanagementservice.entity.enums.TipoDocumento;
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
@Table(name = "documento_projeto")
public class DocumentoProjeto extends BaseAuditingEntity
{
    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @Column(name = "data_criacao")
    private Date dataCriacao;
    @Column(name = "arquivo_path", columnDefinition = "TEXT")
    private String arquivoPath;
    @Column(name = "legenda", columnDefinition = "TEXT")
    private String legenda;
    @Enumerated(EnumType.STRING)
    private TipoDocumento tipoDocumento;
    @Enumerated(EnumType.STRING)
    private Formato formato;
    @Column(name = "status")
    private Boolean status;

    @ManyToOne(fetch = FetchType.LAZY) // Lazy é melhor para performance
    @JoinColumn(name = "organizacao_id", referencedColumnName = "id")
    private Organizacao organizacaoId;
    @ManyToOne(fetch = FetchType.LAZY) // Lazy é melhor para performance
    @JoinColumn(name = "arquitecto_desenho_id", referencedColumnName = "id")
    private Funcionario arquitectoDesenhoId;
    @ManyToOne(fetch = FetchType.LAZY) // Lazy é melhor para performance
    @JoinColumn(name = "versao_projeto_id", referencedColumnName = "id")
    private VersaoProjeto versaoProjetoId;

}