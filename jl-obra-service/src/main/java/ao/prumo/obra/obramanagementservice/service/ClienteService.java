package ao.prumo.obra.obramanagementservice.service;

import ao.prumo.obra.obramanagementservice.entity.Cargo;
import ao.prumo.obra.obramanagementservice.entity.Cliente;
import ao.prumo.obra.obramanagementservice.entity.repository.ClienteRepository;
import ao.prumo.obra.obramanagementservice.utils.base.BaseService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Getter
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ClienteService extends BaseService<Cliente, UUID>
{

    private final ClienteRepository repository;

    protected JpaRepository<Cliente, UUID> getRepository() {
        return this.repository;
    }

}
