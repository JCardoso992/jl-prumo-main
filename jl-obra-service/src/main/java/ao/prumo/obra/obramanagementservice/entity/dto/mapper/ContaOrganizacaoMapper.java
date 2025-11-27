package ao.prumo.obra.obramanagementservice.entity.dto.mapper;

import ao.prumo.obra.obramanagementservice.entity.Agencia;
import ao.prumo.obra.obramanagementservice.entity.ContaOrganizacao;
import ao.prumo.obra.obramanagementservice.entity.dto.request.ContaOrganizacaoRequest;
import ao.prumo.obra.obramanagementservice.entity.dto.response.AgenciaResponse;
import ao.prumo.obra.obramanagementservice.entity.dto.response.ContaOrganizacaoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring", uses = {AgenciaMapper.class},
       unmappedTargetPolicy = ReportingPolicy.IGNORE,
       unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ContaOrganizacaoMapper
{
    ContaOrganizacaoMapper INSTANCE = Mappers.getMapper(ContaOrganizacaoMapper.class);

    // Mapeamento de ContaOrganizacao para ContaOrganizacaoResponse
    @Mapping(source = "id", target = "codContaOrganizacao")
    @Mapping(source = "agenciaId", target = "codContaOrganizacao", qualifiedByName = "mapAgenciaToResponse")
    ContaOrganizacaoResponse toResponse(ContaOrganizacao entity);

    // Mapeamento de ContaOrganizacaoRequest para ContaOrganizacao
    @Mapping(source = "codAgencia", target = "agenciaId", qualifiedByName = "mapCodContaOrganizacaoToEntity")
    // Ignorar as propriedades que são gerenciadas pelo JPA/BD
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedData", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    ContaOrganizacao toEntity(ContaOrganizacaoRequest request);

    List<ContaOrganizacaoResponse> listToResponse(List<ContaOrganizacao> contasDaOrganicacao);

    // Métodos auxiliares para converter IDs para entidades
    @Named("mapCodContaOrganizacaoToEntity")
    default Agencia mapCodContaOrganizacaoToEntity(UUID id) {
        if (id == null) {
            return null;
        }
        return new Agencia(id);
    }

    // Método para mapear Agencia para AgenciaResponse (usado nos mapeamentos acima)
    @Named("mapAgenciaToResponse")
    default AgenciaResponse mapAgenciaToResponse(Agencia entity) {
        if (entity == null) {
            return null;
        }
        return AgenciaMapper.INSTANCE.toResponse(entity);
    }
}
