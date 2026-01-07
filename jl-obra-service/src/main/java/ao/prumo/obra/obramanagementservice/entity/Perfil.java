package ao.prumo.obra.obramanagementservice.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode; // Certifique-se de importar
import org.hibernate.type.SqlTypes;
import lombok.*;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "perfis")
public class Perfil { 

    @Id
    private UUID id; // Este ID é o 'sub' do Supabase

    @Column(name = "nome_completo")
    private String nomeCompleto;

    private String email;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", columnDefinition = "user_role")
    @JdbcTypeCode(SqlTypes.NAMED_ENUM) // <--- Esta é a anotação específica para Enums nativos
    private UserRole role;

    @Column(name = "acesso_liberado")
    private Boolean acessoLiberado;

    @ManyToOne(fetch = FetchType.LAZY) // Lazy é melhor para performance
    @JoinColumn(name = "organizacao_id", referencedColumnName = "id")
    private Organizacao organizacaoId;
}