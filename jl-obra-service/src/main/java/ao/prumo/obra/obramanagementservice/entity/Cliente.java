package ao.prumo.obra.obramanagementservice.entity;


import ao.prumo.obra.obramanagementservice.entity.enums.EstadoCliente;
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
@Table(name = "cliente")
public class Cliente extends BaseAuditingEntity
{
    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;
    @Column(name = "telefone1", columnDefinition = "TEXT")
    private String telefone1;
    @Column(name = "telefone2", columnDefinition = "TEXT")
    private String telefone2;
    @Column(name = "email", columnDefinition = "TEXT")
    private String email;
    @Column(name = "fax", columnDefinition = "TEXT")
    private String fax;
    @Column(name = "arquivo_path", columnDefinition = "TEXT")
    private String arquivoPath;
    @Column(name = "endereco_web", columnDefinition = "TEXT")
    private String enderecoWeb;
    @Column(name = "status")
    private Boolean status;

    @Enumerated(EnumType.STRING)
    private EstadoCliente estadoCliente;

    @ManyToOne
    @JoinColumn(name = "organizacao_id", referencedColumnName = "id")
    private Organizacao organizacaoId;
    @ManyToOne
    @JoinColumn(name = "pessoa_id", referencedColumnName = "id")
    private Pessoa pessoaId;

    public Cliente(UUID codcliente)
    {
        this.id = codcliente;
    }
}
