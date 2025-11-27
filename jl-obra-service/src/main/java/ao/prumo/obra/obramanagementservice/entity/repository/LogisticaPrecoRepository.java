package ao.prumo.obra.obramanagementservice.entity.repository;

import ao.prumo.obra.obramanagementservice.entity.LogisticaPreco;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LogisticaPrecoRepository extends JpaRepository<LogisticaPreco, UUID>
{
}
