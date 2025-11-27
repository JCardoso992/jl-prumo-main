package ao.prumo.obra.obramanagementservice.entity.dto.mapper;

import ao.prumo.obra.obramanagementservice.entity.Endereco;
import ao.prumo.obra.obramanagementservice.entity.dto.request.EnderecoRequest;
import ao.prumo.obra.obramanagementservice.entity.dto.response.EnderecoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EnderecoMapper
{
    EnderecoMapper INSTANCE = Mappers.getMapper(EnderecoMapper.class);

    // Mapeamento de Endereco para EnderecoResponse
    @Mapping(source = "id", target = "codEndereco")
    EnderecoResponse toResponse(Endereco entity);

    // Mapeamento de EnderecoRequest para Endereco
    // Ignorar as propriedades que são gerenciadas pelo JPA/BD
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedData", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    Endereco toEntity(EnderecoRequest request);

    List<EnderecoResponse> listToResponse(List<Endereco> enderecos);
}
