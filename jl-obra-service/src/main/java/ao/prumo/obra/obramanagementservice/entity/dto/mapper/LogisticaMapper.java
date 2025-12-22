package ao.prumo.obra.obramanagementservice.entity.dto.mapper;

import ao.prumo.obra.obramanagementservice.entity.Logistica;
import ao.prumo.obra.obramanagementservice.entity.Despesa;
import ao.prumo.obra.obramanagementservice.entity.dto.request.LogisticaRequest;
import ao.prumo.obra.obramanagementservice.entity.dto.response.LogisticaResponse;
import ao.prumo.obra.obramanagementservice.entity.dto.response.DespesaResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses = {DespesaMapper.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface LogisticaMapper
{
    LogisticaMapper INSTANCE = Mappers.getMapper(LogisticaMapper.class);

    // Mapeamento de Logistica para LogisticaResponse
    @Mapping(source = "id", target = "codLogistiga")
    @Mapping(source = "mercadoriaId", target = "codMercadoria", qualifiedByName = "mapDespesaToResponse")
    LogisticaResponse toResponse(Logistica entity);

    // Mapeamento de LogisticaRequest para Logistica
    // Ignorar as propriedades que são gerenciadas pelo JPA/BD
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedData", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    Logistica toEntity(LogisticaRequest req);

    List<LogisticaResponse> listToResponse(List<Logistica> itens);

    // Método para mapear Despesa para DespesaResponse (usado nos mapeamentos acima)
    @Named("mapDespesaToResponse")
    default DespesaResponse mapDespesaToResponse(Despesa entity) {
        if (entity == null) {
            return null;
        }
        return DespesaMapper.INSTANCE.toResponse(entity);
    }

}        