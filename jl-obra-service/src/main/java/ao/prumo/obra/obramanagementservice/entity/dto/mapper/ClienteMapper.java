package ao.prumo.obra.obramanagementservice.entity.dto.mapper;

import ao.prumo.obra.obramanagementservice.entity.Cliente;
import ao.prumo.obra.obramanagementservice.entity.Pessoa;
import ao.prumo.obra.obramanagementservice.entity.dto.request.ClienteRequest;
import ao.prumo.obra.obramanagementservice.entity.dto.request.PessoaRequest;
import ao.prumo.obra.obramanagementservice.entity.dto.response.ClienteResponse;
import ao.prumo.obra.obramanagementservice.entity.dto.response.PessoaResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses = {PessoaMapper.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ClienteMapper
{
    ClienteMapper INSTANCE = Mappers.getMapper(ClienteMapper.class);

    // Mapeamento de Cliente para ClienteResponse
    @Mapping(source = "id", target = "codCliente")
    @Mapping(source = "pessoaId", target = "codPessoa", qualifiedByName = "mapPessoaToResponse")
    ClienteResponse toResponse(Cliente entity);

    // Mapeamento de ClienteRequest para Cliente
    @Mapping(source = "codPessoaId", target = "pessoaId", qualifiedByName = "mapPessoaRequestToEntity")
    // Ignorar as propriedades que são gerenciadas pelo JPA/BD
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedData", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    Cliente toEntity(ClienteRequest req);

    List<ClienteResponse> listToResponse(List<Cliente> clientes);

    // Método para mapear PessoaRequest para Pessoa (usado nos mapeamentos acima)
    @Named("mapPessoaRequestToEntity")
    default Pessoa mapPessoaRequestToEntity(PessoaRequest request) {
        if (request == null) {
            return null;
        }
        return PessoaMapper.INSTANCE.toEntity(request);
    }

    // Método para mapear Pessoa para PessoaResponse (usado nos mapeamentos acima)
    @Named("mapPessoaToResponse")
    default PessoaResponse mapPessoaToResponse(Pessoa entity) {
        if (entity == null) {
            return null;
        }
        return PessoaMapper.INSTANCE.toResponse(entity);
    }

}
