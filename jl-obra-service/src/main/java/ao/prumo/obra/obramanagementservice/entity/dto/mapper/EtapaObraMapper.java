package ao.prumo.obra.obramanagementservice.entity.dto.mapper;

import ao.prumo.obra.obramanagementservice.entity.EtapaObra;
import ao.prumo.obra.obramanagementservice.entity.Obra;
import ao.prumo.obra.obramanagementservice.entity.Despesa;
import ao.prumo.obra.obramanagementservice.entity.ContaOrganizacao;
import ao.prumo.obra.obramanagementservice.entity.PagamentoProjecto;
import ao.prumo.obra.obramanagementservice.entity.dto.request.EtapaObraRequest;
import ao.prumo.obra.obramanagementservice.entity.dto.response.EtapaObraResponse;
import ao.prumo.obra.obramanagementservice.entity.dto.response.ObraResponse;
import ao.prumo.obra.obramanagementservice.entity.dto.response.DespesaResponse;
import ao.prumo.obra.obramanagementservice.entity.dto.response.ContaOrganizacaoResponse;
import ao.prumo.obra.obramanagementservice.entity.dto.response.PagamentoProjectoResponse;
import ao.prumo.obra.obramanagementservice.utils.base.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ObraMapper.class, DespesaMapper.class, ContaOrganizacaoMapper.class, PagamentoProjectoMapper.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface EtapaObraMapper extends BaseMapper<EtapaObra, EtapaObraRequest, EtapaObraResponse>
{
     EtapaObraMapper INSTANCE = Mappers.getMapper(EtapaObraMapper.class);

     // Mapeamento de EtapaObra para EtapaObraResponse
     @Mapping(source = "id", target = "codEtapaObra")
     @Mapping(source = "obraId", target = "codObra", qualifiedByName = "mapObraToResponse")
     @Mapping(source = "despesaId", target = "codDespesa", qualifiedByName = "mapDespesaToResponse")
     @Mapping(source = "contaOrganizacaoId", target = "codContaOrganizacao", qualifiedByName = "mapContaToResponse")
     @Mapping(source = "pagamentoProjectoId", target = "codPagamentoProjecto", qualifiedByName = "mapPagamentoProjectoToResponse")
     EtapaObraResponse toResponse(EtapaObra entity);
     
     // Mapeamento de EtapaObraRequest para Cliente
     // Ignorar as propriedades que são gerenciadas pelo JPA/BD
     @Mapping(target = "id", ignore = true)
     @Mapping(target = "createdDate", ignore = true)
     @Mapping(target = "lastModifiedData", ignore = true)
     @Mapping(target = "createdBy", ignore = true)
     @Mapping(target = "lastModifiedBy", ignore = true)
     EtapaObra toEntity(EtapaObraRequest req);

     List<EtapaObraResponse> listToResponse(List<EtapaObra> despesaObras);

     @Named("mapObraToResponse")
     default ObraResponse mapObraToResponse(Obra entity) {
        if (entity == null) {
            return null;
        }
        return ObraMapper.INSTANCE.toResponse(entity);
     }

     @Named("mapDespesaToResponse")
     default DespesaResponse mapDespesaToResponse(Despesa entity) {
        if (entity == null) {
            return null;
        }
        return DespesaMapper.INSTANCE.toResponse(entity);
     }

     @Named("mapContaToResponse")
     default ContaOrganizacaoResponse mapContaToResponse(ContaOrganizacao entity) {
        if (entity == null) {
            return null;
        }
        return ContaOrganizacaoMapper.INSTANCE.toResponse(entity);
     }

     @Named("mapPagamentoProjectoToResponse")
     default PagamentoProjectoResponse mapPagamentoProjectoToResponse(PagamentoProjecto entity) {
        if (entity == null) {
            return null;
        }
        return PagamentoProjectoMapper.INSTANCE.toResponse(entity);
     }
     
}        