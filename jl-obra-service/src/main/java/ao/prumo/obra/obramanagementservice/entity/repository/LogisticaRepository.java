package ao.prumo.obra.obramanagementservice.entity.repository;

import ao.prumo.obra.obramanagementservice.entity.Logistica;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LogisticaRepository extends JpaRepository<Logistica, UUID>
{
}
