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
@Table(name = "agencia")
public class Agencia extends BaseAuditingEntity
{
    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @Column(name = "abreviacao", columnDefinition = "TEXT")
    private String abreviacao;
    @Column(name = "descricao", columnDefinition = "TEXT")
    private String descricao;
    @Column(name = "arquivo_path", columnDefinition = "TEXT")
    private String arquivoPath;

    public Agencia(UUID codAgencia) {
        this.id = codAgencia;
    }
}