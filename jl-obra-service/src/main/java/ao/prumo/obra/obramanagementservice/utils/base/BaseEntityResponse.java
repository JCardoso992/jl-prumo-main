package ao.prumo.obra.obramanagementservice.utils.base;

import org.springframework.data.domain.Page;

import java.util.HashMap;
import java.util.List;

public abstract class BaseEntityResponse<E, T>
{
    public abstract T convertToDTO(E entity);

    public abstract List<T> listToDTO(List<E> list);

    public HashMap paginar(Page page )
    {
        HashMap<String, Object> response = new HashMap<String, Object>();

        response.put("content", this.listToDTO(page.getContent()) );
        response.put("empty", page.isEmpty());
        response.put("last", page.isLast());
        response.put("first", page.isFirst());
        response.put("numberOfElements", page.getNumberOfElements() );
        response.put("size", page.getSize() );
        response.put("page", page.getNumber() );

        return response;
    }
}
