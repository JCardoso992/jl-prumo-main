package ao.prumo.obra.obramanagementservice.entity.repository;

import ao.prumo.obra.obramanagementservice.entity.ClienteView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

import java.util.UUID;

@Repository
public interface ClienteViewRepository extends JpaRepository<ClienteView, UUID>
{
    Page<ClienteView> findByOrganizacaoId(UUID organizacaoId, Pageable pageable);
}