package ao.prumo.obra.obramanagementservice.entity.dto.mapper;

import ao.prumo.obra.obramanagementservice.entity.*;
import ao.prumo.obra.obramanagementservice.entity.dto.request.FuncionarioRequest;
import ao.prumo.obra.obramanagementservice.entity.dto.request.PessoaRequest;
import ao.prumo.obra.obramanagementservice.entity.dto.response.FuncionarioResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses={PessoaMapper.class, AgenciaMapper.class, CargoMapper.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface  FuncionarioMapper
{
   FuncionarioMapper INSTANCE = Mappers.getMapper(FuncionarioMapper.class);

    // Mapeamento de Funcionario para FuncionarioResponse
    @Mapping(source = "id", target = "codFuncionario")
    @Mapping(source = "agenciaId", target = "codAgencia")
    @Mapping(source = "cargoId", target = "codCargo")
    @Mapping(source = "organizacaoId", target = "codOrganizacao")
    @Mapping(source = "pessoaId", target = "codPessoa")
    FuncionarioResponse toResponse(Funcionario entity);

    // Mapeamento de FuncionarioRequest para Funcionario
    @Mapping(source = "codAgencia", target = "agenciaId")
    @Mapping(source = "codCargo", target = "cargoId")
    @Mapping(source = "codOrganizacao", target = "organizacaoId")
    @Mapping(target = "pessoaId", source = "codPessoa") // Mapeamento do objeto aninhado
    @Mapping(target = "keycloak_id", source = "keycloak_id")

    // Ignorar as propriedades que são gerenciadas pelo JPA/BD
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedData", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    Funcionario toEntity(FuncionarioRequest req);

    List<FuncionarioResponse> listToResponse(List<Funcionario> funcionarios);
/*
    // Métodos auxiliares para converter IDs para entidades
    default Agencia mapAgenciaId(UUID id) {
        if (id == null) {
            return null;
        }
        return new Agencia(id);
    }

    default Cargo mapCargoId(UUID id) {
        if (id == null) {
            return null;
        }
        return new Cargo(id);
    }

    default Organizacao mapOrganizacaoId(UUID id) {
        if (id == null) {
            return null;
        }
        return new Organizacao(id);
    }

   default Pessoa mapPessoa(PessoaRequest pessoaRequest) {
        if (pessoaRequest == null) {
            return null;
        }
        return new Pessoa(pessoaRequest.getId());
    }*/
}
