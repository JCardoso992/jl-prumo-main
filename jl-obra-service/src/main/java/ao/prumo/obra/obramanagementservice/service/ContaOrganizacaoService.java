package ao.prumo.obra.obramanagementservice.service;

import ao.prumo.obra.obramanagementservice.entity.Cliente;
import ao.prumo.obra.obramanagementservice.entity.ContaOrganizacao;
import ao.prumo.obra.obramanagementservice.entity.repository.ContaOrganizacaoRepository;
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
public class ContaOrganizacaoService extends BaseService<ContaOrganizacao, UUID> {

    private final ContaOrganizacaoRepository repository;

    protected JpaRepository<ContaOrganizacao, UUID> getRepository() {
        return this.repository;
    }
}