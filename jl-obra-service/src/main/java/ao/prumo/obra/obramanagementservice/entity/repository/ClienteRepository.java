package ao.prumo.obra.obramanagementservice.entity.repository;

import ao.prumo.obra.obramanagementservice.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

import java.util.UUID;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, UUID>
{
    @Query("SELECT c FROM Cliente c WHERE c.pessoaId.organizacaoId.id = :organizacaoId")
    Page<Cliente> findByOrganizacaoId(@Param("organizacaoId") UUID organizacaoId, Pageable pageable);

    @Query("SELECT c FROM Cliente c WHERE c.id = :id AND c.pessoaId.organizacaoId.id = :organizacaoId")
    Optional<Cliente> findByIdAndOrganizacaoId(@Param("id") UUID id, @Param("organizacaoId") UUID organizacaoId);
}
