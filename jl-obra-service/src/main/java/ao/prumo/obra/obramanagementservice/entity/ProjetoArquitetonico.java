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
@Table(name = "projeto_arquitetonico")
public class ProjetoArquitetonico  extends BaseAuditingEntity
{
    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;
    @Column(name = "codigo_processo", columnDefinition = "TEXT")
    private String codigoProcesso;
    @Column(name = "nota_informativa")
    private String notaInformativa;
    @Column(name = "descricao")
    private String descricao;
    @Column(name = "data_inicio")
    private Date dataInicio;
    @Column(name = "data_termino")
    private Date dataTermino;
    @Column(name = "orcamento_total")
    private Double orcamentoTotal;
    @Column(name = "saldo_caixa")
    private Double saldoCaixa;
    @Column(name = "status")
    private Boolean status;

    @ManyToOne
    @JoinColumn(name = "cliente_id", referencedColumnName = "id")
    private Cliente clienteId;
    @ManyToOne
    @JoinColumn(name = "organizacao_id", referencedColumnName = "id")
    private Organizacao organizacaoId;
    @ManyToOne
    @JoinColumn(name = "endereco_id", referencedColumnName = "id")
    private Endereco enderecoId;
    @ManyToOne
    @JoinColumn(name = "arquitecto_chefe_id", referencedColumnName = "id")
    private Funcionario arquitectoChefeId;

    public ProjetoArquitetonico(UUID codProjetoArquitetonico)
    {
        this.id = codProjetoArquitetonico;
    }

}