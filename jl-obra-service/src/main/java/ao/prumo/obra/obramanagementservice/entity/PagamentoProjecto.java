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
@Table(name = "pagamento_projecto")
public class PagamentoProjecto extends BaseAuditingEntity
{
    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;
    @Column(name = "valor_pago")
    private Double ValorPago;
    @Column(name = "valor_remanescente")
    private Double ValorRemanescente;
    @Column(name = "valor_usado")
    private Double ValorUsado;
    @Column(name = "data_pagamento")
    private Date dataPagamento;
    @Column(name = "descricao", columnDefinition = "TEXT")
    private String descricao;
    @Column(name = "status")
    private Boolean status;

    @ManyToOne
    @JoinColumn(name = "projeto_arquitetonico_id", referencedColumnName = "id")
    private ProjetoArquitetonico projetoArquitetonicoId;
    @ManyToOne
    @JoinColumn(name = "conta_organizacao_id", referencedColumnName = "id")
    private ContaOrganizacao contaOrganizacaoId;
    @ManyToOne
    @JoinColumn(name = "organizacao_id", referencedColumnName = "id")
    private Organizacao organizacaoId;

    public PagamentoProjecto(UUID codPagamentoProjecto)
    {
        this.id = codPagamentoProjecto;
    }
}