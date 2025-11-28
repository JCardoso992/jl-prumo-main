package ao.prumo.obra.obramanagementservice.entity.dto.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import ao.prumo.obra.obramanagementservice.entity.dto.request.DocumentoProjetoRequest;

@Mapper(componentModel = "spring", uses = {FuncionarioMapper.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface DocumentoProjetoMapper 
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
    }

}
