package ao.prumo.obra.obramanagementservice.entity.dto.mapper;

import ao.prumo.obra.obramanagementservice.entity.Logistica;
import ao.prumo.obra.obramanagementservice.entity.dto.response.LogisticaResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses = {LogistigaMapper.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface LogisticaPrecoMapper
{
      LogisticaPrecoMapper INSTANCE = Mappers.getMapper(LogisticaPrecoMapper.class);

      // Mapeamento de LogisticaPreco para LogisticaPrecoResponse
       @Mapping(source = "id", target = "codLogisticaPreco")
       @Mapping(source = "logistigaId", target = "codLogistiga", qualifiedByName = "mapLogisticaToResponse")
       LogisticaPrecoResponse toResponse(LogisticaPreco entity);

       List<LogisticaPrecoResponse> listToResponse(List<LogisticaPreco> logisticaPrecos);

       // Método para mapear Logistica para LogisticaResponse (usado nos mapeamentos acima)
       @Named("mapLogisticaToResponse")
       default LogisticaResponse mapLogisticaToResponse(Logistica entity) {
        if (entity == null) {
            return null;
        }
        return LogisticaMapper.INSTANCE.toResponse(entity);
       }

}