package ao.prumo.obra.obramanagementservice.utils.base;

import ao.prumo.obra.obramanagementservice.utils.exception.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.NoSuchElementException;

public abstract class BaseService <T,E>
{
    protected JpaRepository<T, E> repository;

    public List<T> findAll() {
        return this.repository.findAll();
    }

    public Page<T> findAll(Pageable pageable) {
        return this.repository.findAll(pageable);
    }

    public T findById(E id) throws EntityNotFoundException {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Registro não encontrado com o id: " + id));
    }

    public T save(T entidade) {
        return this.repository.save(entidade);
    }

    public T update(E id, T entidade) throws EntityNotFoundException
    {
        if (!this.repository.existsById(id)) {
            throw new ResourceNotFoundException("O registro a ser atualizado não foi encontrado com o id: " + id);
        }
        return this.repository.save(entidade);
    }
}
