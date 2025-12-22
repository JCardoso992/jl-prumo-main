package ao.prumo.obra.obramanagementservice.entity.dto.mapper;

import ao.prumo.obra.obramanagementservice.entity.Obra;
import ao.prumo.obra.obramanagementservice.entity.Funcionario;
import ao.prumo.obra.obramanagementservice.entity.VersaoProjeto;
import ao.prumo.obra.obramanagementservice.entity.dto.request.ObraRequest;
import ao.prumo.obra.obramanagementservice.entity.dto.response.FuncionarioResponse;
import ao.prumo.obra.obramanagementservice.entity.dto.response.ObraResponse;
import ao.prumo.obra.obramanagementservice.entity.dto.response.VersaoProjetoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses = {FuncionarioMapper.class, VersaoProjetoMapper.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ObraMapper
{
    ObraMapper INSTANCE = Mappers.getMapper(ObraMapper.class);

    // Mapeamento de Obra para ObraResponse
    @Mapping(source = "id", target = "codObra")
    @Mapping(source = "encarregadoId", target = "codEncarregado", qualifiedByName = "mapEncarregadoToResponse")
    @Mapping(source = "versaoProjetoId", target = "codVersaoProjeto", qualifiedByName = "mapVersaoProjetoToResponse")
    ObraResponse toResponse(Obra entity);

    // Mapeamento de ObraRequest para Obra
    //@Mapping(source = "codPessoaId", target = "pessoaId", qualifiedByName = "mapPessoaRequestToEntity")
    // Ignorar as propriedades que são gerenciadas pelo JPA/BD
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedData", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    Obra toEntity(ObraRequest req);

    List<ObraResponse> listToResponse(List<Obra> obras);

    // Método para mapear PessoaRequest para Pessoa (usado nos mapeamentos acima)
    /*@Named("mapPessoaRequestToEntity")
    default Pessoa mapPessoaRequestToEntity(PessoaRequest request) {
        if (request == null) {
            return null;
        }
        return PessoaMapper.INSTANCE.toEntity(request);
    }*/

    // Método para mapear Funcionario para FuncionarioResponse (usado nos mapeamentos acima)
    @Named("mapEncarregadoToResponse")
    default FuncionarioResponse mapEncarregadoToResponse(Funcionario entity) {
        if (entity == null) {
            return null;
        }
        return FuncionarioMapper.INSTANCE.toResponse(entity);
    }

    @Named("mapVersaoProjetoToResponse")
    default VersaoProjetoResponse mapVersaoProjetoToResponse(VersaoProjeto entity) {
        if (entity == null) {
            return null;
        }
        return VersaoProjetoMapper.INSTANCE.toResponse(entity);
    }

}