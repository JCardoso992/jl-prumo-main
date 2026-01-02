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
@Table(name = "organizacao")
public class Organizacao extends BaseAuditingEntity
{
    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;
    @Column(name = "telefone1", columnDefinition = "TEXT")
    private String telefone1;
    @Column(name = "telefone2", columnDefinition = "TEXT")
    private String telefone2;
    @Column(name = "email1", columnDefinition = "TEXT")
    private String email1;
    @Column(name = "email2", columnDefinition = "TEXT")
    private String email2;
    @Column(name = "objecto", columnDefinition = "TEXT")
    private String objecto;
    @Column(name = "nif", columnDefinition = "TEXT")
    private String nif;
    @Column(name = "num_matricula", columnDefinition = "TEXT")
    private String numMatricula;
    @Column(name = "firma", columnDefinition = "TEXT")
    private String firma;
    @Column(name = "razao_social", columnDefinition = "TEXT")
    private String razaoSocial;
    @Column(name = "arquivo_path", columnDefinition = "TEXT")
    private String arquivoPath;
    @Column(name = "web_page", columnDefinition = "TEXT")
    private String webPage;
    @Column(name = "regime", columnDefinition = "TEXT")
    private String regime;
    @Column(name = "status")
    private Boolean status;
    //KeyCloak
    private UUID keycloak_realm_id;

    @ManyToOne(fetch = FetchType.LAZY) // Lazy é melhor para performance
    @JoinColumn(name = "adress", referencedColumnName = "id")
    private Endereco adress;

    public Organizacao(UUID codOrganizacao) {
        this.id = codOrganizacao;
    }
}
