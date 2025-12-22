package ao.prumo.obra.obramanagementservice.entity.dto.mapper;

import ao.prumo.obra.obramanagementservice.entity.Despesa;
import ao.prumo.obra.obramanagementservice.entity.dto.request.DespesaRequest;
import ao.prumo.obra.obramanagementservice.entity.dto.response.DespesaResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface DespesaMapper
{
    DespesaMapper INSTANCE = Mappers.getMapper(DespesaMapper.class);

    // Mapeamento de Despesa para DespesaResponse
    @Mapping(source = "id", target = "codDespesa")
    @Mapping(source = "despesaPaiId", target = "codDespesaPai", qualifiedByName = "mapDespesaResponse")

    DespesaResponse toResponse(Despesa entity);

    // Mapeamento de DespesaRequest para Despesa
    @Mapping(source = "codDespesaPai", target = "despesaPaiId", qualifiedByName = "mapCodDespesaRequestToEntity")
    // Ignorar as propriedades que são gerenciadas pelo JPA/BD
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedData", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    Despesa toEntity(DespesaRequest req);

    List<DespesaResponse> listToResponse(List<Despesa> pessoas);

    // Método para mapear DespesaRequest para Despesa (usado nos mapeamentos acima)
    @Named("mapCodDespesaRequestToEntity")
    default Despesa mapDespesaRequestToEntity(UUID id) {
        return id == null ? null : new Despesa(id);
    }
    // Método para mapear Despesa para DespesaResponse (usado nos mapeamentos acima)
    @Named("mapDespesaResponse")
    default DespesaResponse mapDespesaResponse(Despesa entity) {
        if (entity == null) {
            return null;
        }
        return DespesaMapper.INSTANCE.toResponse(entity);
    }
}
