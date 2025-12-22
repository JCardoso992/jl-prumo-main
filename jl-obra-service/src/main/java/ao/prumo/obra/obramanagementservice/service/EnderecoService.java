package ao.prumo.obra.obramanagementservice.service;

import ao.prumo.obra.obramanagementservice.entity.Endereco;
import ao.prumo.obra.obramanagementservice.entity.repository.EnderecoRepository;
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
public class EnderecoService extends BaseService<Endereco, UUID> {

    private final EnderecoRepository repository;

    protected JpaRepository<Endereco, UUID> getRepository() {
        return this.repository;
    }
}
