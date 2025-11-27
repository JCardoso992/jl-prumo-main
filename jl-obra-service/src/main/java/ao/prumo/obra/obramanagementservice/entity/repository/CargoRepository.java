package ao.prumo.obra.obramanagementservice.entity.repository;

import ao.prumo.obra.obramanagementservice.entity.Cargo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CargoRepository extends JpaRepository<Cargo, UUID>
{
}
