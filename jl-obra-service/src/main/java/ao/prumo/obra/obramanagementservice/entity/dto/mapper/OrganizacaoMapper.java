package ao.prumo.obra.obramanagementservice.entity.dto.mapper;

import ao.prumo.obra.obramanagementservice.entity.Endereco;
import ao.prumo.obra.obramanagementservice.entity.Organizacao;
import ao.prumo.obra.obramanagementservice.entity.dto.request.EnderecoRequest;
import ao.prumo.obra.obramanagementservice.entity.dto.request.OrganizacaoRequest;
import ao.prumo.obra.obramanagementservice.entity.dto.response.EnderecoResponse;
import ao.prumo.obra.obramanagementservice.entity.dto.response.OrganizacaoResponse;
import ao.prumo.obra.obramanagementservice.utils.base.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", uses = {EnderecoMapper.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface OrganizacaoMapper extends BaseMapper<Organizacao, OrganizacaoRequest, OrganizacaoResponse>
{
    OrganizacaoMapper INSTANCE = Mappers.getMapper(OrganizacaoMapper.class);

    // Mapeamento de Organizacao para OrganizacaoResponse
    @Mapping(source = "id", target = "codOrganizacao")
    @Mapping(source = "adress", target = "codAdress", qualifiedByName = "mapEnderecoToResponse")
    OrganizacaoResponse toResponse(Organizacao entity);

    // Mapeamento de OrganizacaoRequest para Organizacao
    //@Mapping(source = "codAdress", target = "adress", qualifiedByName = "mapEnderecoRequestToEntity")

    // Ignorar as propriedades que são gerenciadas pelo JPA/BD
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedData", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    Organizacao toEntity(OrganizacaoRequest request);

    List<OrganizacaoResponse> listToResponse(List<Organizacao> organizacoes);

    // Método para mapear EnderecoRequest para Endereco (usado nos mapeamentos acima)
    @Named("mapEnderecoRequestToEntity")
    default Endereco mapEnderecoRequestToEntity(EnderecoRequest request) {
        if (request == null) {
            return null;
        }
        return EnderecoMapper.INSTANCE.toEntity(request);
    }

    // Método para mapear Endereco para EnderecoResponse (usado nos mapeamentos acima)
    @Named("mapEnderecoToResponse")
    default EnderecoResponse mapEnderecoToResponse(Endereco entity) {
        if (entity == null) {
            return null;
        }
        return EnderecoMapper.INSTANCE.toResponse(entity);
    }
}
