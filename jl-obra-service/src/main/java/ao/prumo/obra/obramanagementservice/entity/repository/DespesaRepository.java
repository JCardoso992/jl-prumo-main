package ao.prumo.obra.obramanagementservice.entity.repository;

import ao.prumo.obra.obramanagementservice.entity.Despesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;


@Repository
public interface DespesaRepository extends JpaRepository<Despesa, UUID>
{
    @Query("SELECT d FROM Despesa d WHERE d.organizacaoId = :organizacaoId AND d.status IS TRUE")
    Page<Despesa> findByOrganizacaoId(@Param("organizacaoId") UUID organizacaoId, Pageable pageable);

    @Query("SELECT d FROM Despesa d WHERE d.id = :id AND d.organizacaoId.id = :organizacaoId  AND d.status IS TRUE")
    Optional<Despesa> findByIdAndOrganizacaoId(@Param("id") UUID id, @Param("organizacaoId") UUID organizacaoId);
}
