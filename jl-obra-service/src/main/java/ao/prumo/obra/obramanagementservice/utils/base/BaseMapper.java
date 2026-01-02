package ao.prumo.obra.obramanagementservice.utils.base;

import org.mapstruct.BeanMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

public interface BaseMapper<E, REQ, RES>
{
    E toEntity(REQ dto);
    RES toResponse(E entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(REQ dto, @MappingTarget E entity);
}