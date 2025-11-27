package ao.prumo.obra.obramanagementservice.entity.dto.mapper;

import ao.prumo.obra.obramanagementservice.entity.Cargo;
import ao.prumo.obra.obramanagementservice.entity.dto.request.CargoRequest;
import ao.prumo.obra.obramanagementservice.entity.dto.response.CargoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CargoMapper
{
    CargoMapper INSTANCE = Mappers.getMapper(CargoMapper.class);

    // Mapeamento de Cargo para CargoResponse
    @Mapping(source = "id", target = "codCargo")
    CargoResponse toResponse(Cargo entity);

    // Mapeamento de CargoRequest para Cargo
    // Ignorar as propriedades que são gerenciadas pelo JPA/BD
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedData", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    Cargo toEntity(CargoRequest request);

    List<CargoResponse> listToResponse(List<Cargo> entity);

}
