package ao.prumo.obra.obramanagementservice.entity.dto.mapper;

import java.util.List;

import ao.prumo.obra.obramanagementservice.entity.DocumentoProjeto;
import ao.prumo.obra.obramanagementservice.entity.Funcionario;
import ao.prumo.obra.obramanagementservice.entity.dto.response.DocumentoProjetoResponse;
import ao.prumo.obra.obramanagementservice.entity.dto.request.DocumentoProjetoRequest;
import ao.prumo.obra.obramanagementservice.entity.dto.response.FuncionarioResponse;
import ao.prumo.obra.obramanagementservice.utils.base.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;


@Mapper(componentModel = "spring", uses = {FuncionarioMapper.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface DocumentoProjetoMapper extends BaseMapper<DocumentoProjeto, DocumentoProjetoRequest, DocumentoProjetoResponse> 
{
        // Mapeamento de DocumentoProjeto para DocumentoProjetoResponse
        @Mapping(source = "id", target = "codDocumentoProjeto")
        @Mapping(source = "arquitectoDesenhoId", target = "codArquitectoDesenho", qualifiedByName = "mapFuncionarioToResponse")
        DocumentoProjetoResponse toResponse(DocumentoProjeto entity);

        // Mapeamento de DocumentoProjetoRequest para DocumentoProjeto
        // Ignorar as propriedades que são gerenciadas pelo JPA/BD
        @Mapping(target = "id", ignore = true)
        @Mapping(target = "createdDate", ignore = true)
        @Mapping(target = "lastModifiedData", ignore = true)
        @Mapping(target = "createdBy", ignore = true)
        @Mapping(target = "lastModifiedBy", ignore = true)
        DocumentoProjeto toEntity(DocumentoProjetoRequest req);

        List<DocumentoProjetoResponse> listToResponse(List<DocumentoProjeto> documentos);

        // Método para mapear Funcionario para FuncionarioResponse (usado nos mapeamentos acima)
        @Named("mapFuncionarioToResponse")
        default FuncionarioResponse mapFuncionarioToResponse(Funcionario entity) {
        if (entity == null) {
            return null;
        }
        return FuncionarioMapper.INSTANCE.toResponse(entity);
        /*
        // ========================= AUX METHOD =========================
        @Named("mapFuncionarioIdToEntity")
        default Funcionario mapFuncionarioIdToEntity(UUID id) {
            if (id == null) return null;
            Funcionario funcionario = new Funcionario();
            funcionario.setId(id);
            return funcionario;
        }
        */
    }

}
