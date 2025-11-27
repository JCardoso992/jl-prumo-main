package ao.prumo.obra.obramanagementservice.service;

import ao.prumo.obra.obramanagementservice.entity.Obra;
import ao.prumo.obra.obramanagementservice.entity.ProjetoArquitetonico;
import ao.prumo.obra.obramanagementservice.entity.repository.ProjetoArquitetonicoRepository;
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
public class ProjetoArquitetonicoService extends BaseService<ProjetoArquitetonico, UUID> {

    private final ProjetoArquitetonicoRepository repository;

    /*protected JpaRepository<ProjetoArquitetonico, UUID> getRepository() {
        return this.repository;
    }*/
}