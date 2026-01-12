package ao.prumo.obra.obramanagementservice.entity.dto.mapper;

import ao.prumo.obra.obramanagementservice.entity.ClienteView;
import ao.prumo.obra.obramanagementservice.entity.dto.response.ClienteViewResponse;
import ao.prumo.obra.obramanagementservice.utils.base.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ClienteViewMapper extends BaseMapper<ClienteView, Void, ClienteViewResponse> 
{
    ClienteViewMapper INSTANCE = Mappers.getMapper(ClienteViewMapper.class);

    @Mapping(source = "clienteId", target = "codCliente")
    @Mapping(source = "pessoaId", target = "codPessoa")
    ClienteViewResponse toResponse(ClienteView entity);

}    