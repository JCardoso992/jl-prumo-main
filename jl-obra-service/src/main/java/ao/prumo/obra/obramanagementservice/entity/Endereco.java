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
@Table(name = "endereco")
public class Endereco extends BaseAuditingEntity
{
    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;
    @Column(name = "endereco", columnDefinition = "TEXT")
    private String endereco;
    @Column(name = "cidade", columnDefinition = "TEXT")
    private String cidade;
    @Column(name = "provincia", columnDefinition = "TEXT")
    private String provincia;
    @Column(name = "pais", columnDefinition = "TEXT")
    private String pais;
    @Column(name = "code_plus", columnDefinition = "TEXT")
    private String codePlus;
    @Column(name = "latitude")
    private Double latitude;
    @Column(name = "longitude")
    private Double longitude;
    @Column(name = "altitude")
    private Double altitude;
    @Column(name = "status")
    private Boolean status;

    public Endereco(UUID codAdress)
    {
        this.id = codAdress;
    }
}
