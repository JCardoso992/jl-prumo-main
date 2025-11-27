package ao.prumo.obra.obramanagementservice.service;

import ao.prumo.obra.obramanagementservice.entity.Despesa;
import ao.prumo.obra.obramanagementservice.entity.DocumentoProjeto;
import ao.prumo.obra.obramanagementservice.entity.repository.DocumentoProjetoRepository;
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
public class DocumentoProjetoService extends BaseService<DocumentoProjeto, UUID> {

    private final DocumentoProjetoRepository repository;

    protected JpaRepository<DocumentoProjeto, UUID> getRepository() {
        return this.repository;
    }
}