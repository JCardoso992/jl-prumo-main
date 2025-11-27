package ao.prumo.obra.obramanagementservice.entity.repository;

import ao.prumo.obra.obramanagementservice.entity.EtapaLogistica;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EtapaLogisticaRepository extends JpaRepository<EtapaLogistica, UUID>
{
}