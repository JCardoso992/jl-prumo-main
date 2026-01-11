package ao.prumo.obra.obramanagementservice.entity.repository;

import ao.prumo.obra.obramanagementservice.entity.FuncionarioView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

import java.util.UUID;

@Repository
public interface FuncionarioViewRepository extends JpaRepository<FuncionarioView, UUID>
{
    Page<FuncionarioView> findByOrganizacaoId(UUID organizacaoId, Pageable pageable);
}