package ao.prumo.obra.obramanagementservice.entity.dto.mapper;

import ao.prumo.obra.obramanagementservice.entity.VersaoProjeto;
import ao.prumo.obra.obramanagementservice.entity.ProjetoArquitetonico;
import ao.prumo.obra.obramanagementservice.entity.dto.request.VersaoProjetoRequest;
import ao.prumo.obra.obramanagementservice.entity.dto.response.VersaoProjetoResponse;
import ao.prumo.obra.obramanagementservice.entity.dto.response.ProjetoArquitetonicoResponse;
import ao.prumo.obra.obramanagementservice.utils.base.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ProjetoArquitetonicoMapper.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface VersaoProjetoMapper extends BaseMapper<VersaoProjeto, VersaoProjetoRequest, VersaoProjetoResponse>
{
    VersaoProjetoMapper INSTANCE = Mappers.getMapper(VersaoProjetoMapper.class);

    // Mapeamento de VersaoProjeto para VersaoProjetoResponse
    @Mapping(source = "id", target = "codVersaoProjeto")
    @Mapping(source = "projetoArquitetonicoId", target = "codProjetoArquitetonico", qualifiedByName = "mapProjetoArquitetonicoToResponse")
    VersaoProjetoResponse toResponse(VersaoProjeto entity);

    // Mapeamento de VersaoProjetoRequest para VersaoProjeto
    //@Mapping(source = "codPessoaId", target = "pessoaId", qualifiedByName = "mapPessoaRequestToEntity")
    // Ignorar as propriedades que são gerenciadas pelo JPA/BD
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedData", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    VersaoProjeto toEntity(VersaoProjetoRequest req);

    List<VersaoProjetoResponse> listToResponse(List<VersaoProjeto> veroesProjeto);

    // Método para mapear PessoaRequest para Pessoa (usado nos mapeamentos acima)
    /*@Named("mapPessoaRequestToEntity")
    default Pessoa mapPessoaRequestToEntity(PessoaRequest request) {
        if (request == null) {
            return null;
        }
        return PessoaMapper.INSTANCE.toEntity(request);
    }*/

    // Método para mapear Pessoa para PessoaResponse (usado nos mapeamentos acima)
    @Named("mapProjetoArquitetonicoToResponse")
    default ProjetoArquitetonicoResponse mapPessoaToResponse(ProjetoArquitetonico entity) {
        if (entity == null) {
            return null;
        }
        return ProjetoArquitetonicoMapper.INSTANCE.toResponse(entity);
    }

}
