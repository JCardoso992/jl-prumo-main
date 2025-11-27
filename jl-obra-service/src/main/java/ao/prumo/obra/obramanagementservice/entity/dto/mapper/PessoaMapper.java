package ao.prumo.obra.obramanagementservice.entity.dto.mapper;

import ao.prumo.obra.obramanagementservice.entity.Endereco;
import ao.prumo.obra.obramanagementservice.entity.Pessoa;
import ao.prumo.obra.obramanagementservice.entity.dto.request.EnderecoRequest;
import ao.prumo.obra.obramanagementservice.entity.dto.request.PessoaRequest;
import ao.prumo.obra.obramanagementservice.entity.dto.response.EnderecoResponse;
import ao.prumo.obra.obramanagementservice.entity.dto.response.PessoaResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;
/*
*unmappedTargetPolicy = ReportingPolicy.ERROR, // Erro se campo de destino não for mapeado
*unmappedSourcePolicy = ReportingPolicy.WARN)  // Aviso se campo de origem não for usado
* */
@Mapper(componentModel = "spring", uses = {EnderecoMapper.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface PessoaMapper
{

    PessoaMapper INSTANCE = Mappers.getMapper(PessoaMapper.class);

    // Mapeamento de Pessoa para PessoaResponse
    @Mapping(source = "id", target = "codPessoa")
    @Mapping(source = "adress1", target = "codAdress1", qualifiedByName = "mapEnderecoToResponse")
    @Mapping(source = "adress2", target = "codAdress2", qualifiedByName = "mapEnderecoToResponse")
    PessoaResponse toResponse(Pessoa entity);

    // Mapeamento de PessoaRequest para Pessoa
    @Mapping(source = "codAdress1", target = "adress1", qualifiedByName = "mapEnderecoRequestToEntity")
    @Mapping(source = "codAdress2", target = "adress2", qualifiedByName = "mapEnderecoRequestToEntity")

    // Ignorar as propriedades que são gerenciadas pelo JPA/BD
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedData", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    Pessoa toEntity(PessoaRequest req);

    List<PessoaResponse> listToResponse(List<Pessoa> pessoas);

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
