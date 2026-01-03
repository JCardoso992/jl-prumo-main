package ao.prumo.obra.obramanagementservice.entity.dto.mapper;

import ao.prumo.obra.obramanagementservice.entity.Agencia;
import ao.prumo.obra.obramanagementservice.entity.dto.request.AgenciaRequest;
import ao.prumo.obra.obramanagementservice.entity.dto.response.AgenciaResponse;
import ao.prumo.obra.obramanagementservice.utils.base.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface AgenciaMapper extends BaseMapper<Agencia, AgenciaRequest, AgenciaResponse>
{
    AgenciaMapper INSTANCE = Mappers.getMapper(AgenciaMapper.class);

    // Mapeamento de Agencia para AgenciaResponse
    @Mapping(source = "id", target = "codAgencia")
    AgenciaResponse toResponse(Agencia entity);

    // Mapeamento de AgenciaRequest para Agencia
    // Ignorar as propriedades que são gerenciadas pelo JPA/BD
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedData", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    Agencia toEntity(AgenciaRequest request);

    List<AgenciaResponse> listaToResponse(List<Agencia> agencias);
}
