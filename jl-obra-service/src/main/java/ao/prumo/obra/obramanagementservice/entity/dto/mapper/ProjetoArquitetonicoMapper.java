package ao.prumo.obra.obramanagementservice.entity.dto.mapper;

import ao.prumo.obra.obramanagementservice.entity.ProjetoArquitetonico;
import ao.prumo.obra.obramanagementservice.entity.Cliente;
import ao.prumo.obra.obramanagementservice.entity.Endereco;
import ao.prumo.obra.obramanagementservice.entity.Funcionario;
import ao.prumo.obra.obramanagementservice.entity.dto.request.ProjetoArquitetonicoRequest;
import ao.prumo.obra.obramanagementservice.entity.dto.response.ProjetoArquitetonicoResponse;
import ao.prumo.obra.obramanagementservice.entity.dto.response.ClienteResponse;
import ao.prumo.obra.obramanagementservice.entity.dto.response.EnderecoResponse;
import ao.prumo.obra.obramanagementservice.entity.dto.response.FuncionarioResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ClienteMapper.class, EnderecoMapper.class, FuncionarioMapper.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ProjetoArquitetonicoMapper
{
    ProjetoArquitetonicoMapper INSTANCE = Mappers.getMapper(ProjetoArquitetonicoMapper.class);

    // Mapeamento de ProjetoArquitetonico para ProjetoArquitetonicoResponse
    @Mapping(source = "id", target = "codProjetoArquitetonico")
    @Mapping(source = "clienteId", target = "codCliente", qualifiedByName = "mapClienteToResponse")
    @Mapping(source = "enderecoId", target = "codEndereco", qualifiedByName = "mapEnderecoToResponse")
    @Mapping(source = "arquitectoChefeId", target = "codArquitectoChefe", qualifiedByName = "mapFuncionarioToResponse")
    ProjetoArquitetonicoResponse toResponse(ProjetoArquitetonico entity);

    // Mapeamento de ProjetoArquitetonicoRequest para ProjetoArquitetonico
    //@Mapping(source = "codPessoaId", target = "pessoaId", qualifiedByName = "mapPessoaRequestToEntity")
    // Ignorar as propriedades que são gerenciadas pelo JPA/BD
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedData", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    ProjetoArquitetonico toEntity(ProjetoArquitetonicoRequest req);

    List<ProjetoArquitetonicoResponse> listToResponse(List<ProjetoArquitetonico> projetosArquitetonicos);

    // Método para mapear PessoaRequest para Pessoa (usado nos mapeamentos acima)
    /*@Named("mapPessoaRequestToEntity")
    default Pessoa mapPessoaRequestToEntity(PessoaRequest request) {
        if (request == null) {
            return null;
        }
        return PessoaMapper.INSTANCE.toEntity(request);
    }*/

    // Método para mapear Pessoa para PessoaResponse (usado nos mapeamentos acima)
    @Named("mapClienteToResponse")
    default ClienteResponse mapClienteToResponse(Cliente entity) {
        if (entity == null) {
            return null;
        }
        return ClienteMapper.INSTANCE.toResponse(entity);
    }

    @Named("mapEnderecoToResponse")
    default EnderecoResponse mapEnderecoToResponse(Endereco entity) {
        if (entity == null) {
            return null;
        }
        return EnderecoMapper.INSTANCE.toResponse(entity);
    }

    @Named("mapFuncionarioToResponse")
    default FuncionarioResponse mapFuncionarioToResponse(Funcionario entity) {
        if (entity == null) {
            return null;
        }
        return FuncionarioMapper.INSTANCE.toResponse(entity);
    }

}
