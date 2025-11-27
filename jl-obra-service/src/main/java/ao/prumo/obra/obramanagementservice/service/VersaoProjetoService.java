package ao.prumo.obra.obramanagementservice.service;


import ao.prumo.obra.obramanagementservice.entity.VersaoProjeto;
import ao.prumo.obra.obramanagementservice.entity.repository.VersaoProjetoRepository;
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
public class VersaoProjetoService extends BaseService<VersaoProjeto, UUID> {

    private final VersaoProjetoRepository repository;

    protected JpaRepository<VersaoProjeto, UUID> getRepository() {
        return this.repository;
    }
}