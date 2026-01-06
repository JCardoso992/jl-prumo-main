package ao.prumo.obra.obramanagementservice.entity.repository;

import ao.prumo.obra.obramanagementservice.entity.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

public interface PerfilRepository extends JpaRepository<Perfil, UUID> 
{
    @Modifying
    @Transactional
    @Query(value = "UPDATE perfis SET organizacao_id = :orgId, role = CAST(:role AS user_role), acesso_liberado = :acesso WHERE id = :usuarioId", nativeQuery = true)
    void atualizarPerfilComoAdmin(
        @Param("orgId") UUID orgId, 
        @Param("role") String role, 
        @Param("acesso") boolean acesso, 
        @Param("usuarioId") UUID usuarioId
    );
}