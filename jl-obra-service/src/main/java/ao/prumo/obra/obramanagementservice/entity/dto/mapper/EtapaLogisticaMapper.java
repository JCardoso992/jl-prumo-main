package ao.prumo.obra.obramanagementservice.entity.dto.mapper;

import ao.prumo.obra.obramanagementservice.entity.EtapaLogistica;
import ao.prumo.obra.obramanagementservice.entity.EtapaObra;
import ao.prumo.obra.obramanagementservice.entity.dto.response.EtapaLogisticaResponse;
import ao.prumo.obra.obramanagementservice.entity.dto.response.EtapaObraResponse;
import ao.prumo.obra.obramanagementservice.utils.base.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses = {EtapaObraMapper.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface EtapaLogisticaMapper extends BaseMapper<EtapaLogistica, EtapaLogisticaResponse, EtapaLogisticaResponse>
{
        EtapaLogisticaMapper INSTANCE = Mappers.getMapper(EtapaLogisticaMapper.class);
        
        // Mapeamento de EtapaLogistica para EtapaLogisticaResponse
        @Mapping(source = "id", target = "codEtapaLogistica")
        @Mapping(source = "etapaObraId", target = "codEtapaObra", qualifiedByName = "mapEtapaObraToResponse")
        EtapaLogisticaResponse toResponse(EtapaLogistica entity);

        List<EtapaLogisticaResponse> listToResponse(List<EtapaLogistica> etapas_items_stock);

        @Named("mapEtapaObraToResponse")
        default EtapaObraResponse mapEtapaObraToResponse(EtapaObra entity) {
        if (entity == null) {
            return null;
        }
        return EtapaObraMapper.INSTANCE.toResponse(entity);
    }

}

