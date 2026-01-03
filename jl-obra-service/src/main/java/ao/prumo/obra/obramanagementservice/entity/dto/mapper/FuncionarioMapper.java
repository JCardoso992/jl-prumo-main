package ao.prumo.obra.obramanagementservice.entity.dto.mapper;

import ao.prumo.obra.obramanagementservice.entity.*;
import ao.prumo.obra.obramanagementservice.entity.dto.request.FuncionarioRequest;
import ao.prumo.obra.obramanagementservice.entity.dto.response.AgenciaResponse;
import ao.prumo.obra.obramanagementservice.entity.dto.response.CargoResponse;
import ao.prumo.obra.obramanagementservice.entity.dto.response.FuncionarioResponse;
import ao.prumo.obra.obramanagementservice.entity.dto.response.PessoaResponse;
import ao.prumo.obra.obramanagementservice.utils.base.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses={PessoaMapper.class, AgenciaMapper.class, CargoMapper.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface FuncionarioMapper extends BaseMapper<Funcionario, FuncionarioRequest, FuncionarioResponse>
{
    FuncionarioMapper INSTANCE = Mappers.getMapper(FuncionarioMapper.class);

    // Mapeamento de Funcionario para FuncionarioResponse
    @Mapping(source = "id", target = "codFuncionario")
    @Mapping(source = "pessoaId", target = "codPessoa", qualifiedByName = "mapPessoaToResponse")
    @Mapping(source = "agenciaId", target = "codAgencia", qualifiedByName = "mapAgenciaToResponse")
    @Mapping(source = "cargoId", target = "codCargo", qualifiedByName = "mapCargoToResponse")
    FuncionarioResponse toResponse(Funcionario entity);

    // Mapeamento de FuncionarioRequest para Funcionario
    /*@Mapping(source = "codAgencia", target = "agenciaId")
    @Mapping(source = "codCargo", target = "cargoId")
    @Mapping(source = "codOrganizacao", target = "organizacaoId")
    @Mapping(target = "pessoaId", source = "codPessoa") // Mapeamento do objeto aninhado
    @Mapping(target = "keycloak_id", source = "keycloak_id")*/

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
    @Named("mapPessoaToResponse")
    default PessoaResponse mapPessoaToResponse(Pessoa entity) {
        if (entity == null) {
            return null;
        }
        return PessoaMapper.INSTANCE.toResponse(entity);
    }

    @Named("mapAgenciaToResponse")
    default AgenciaResponse mapAgenciaToResponse(Agencia entity) {
        if (entity == null) {
            return null;
        }
        return AgenciaMapper.INSTANCE.toResponse(entity);
    }

    @Named("mapCargoToResponse")
    default CargoResponse mapCargoToResponse(Cargo entity) {
        if (entity == null) {
            return null;
        }
        return CargoMapper.INSTANCE.toResponse(entity);
    }
}
