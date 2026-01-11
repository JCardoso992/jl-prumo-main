package ao.prumo.obra.obramanagementservice.entity.dto.mapper;
import ao.prumo.obra.obramanagementservice.entity.FuncionarioView;
import ao.prumo.obra.obramanagementservice.entity.dto.response.FuncionarioViewResponse;
import ao.prumo.obra.obramanagementservice.utils.base.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface FuncionarioViewMapper extends BaseMapper<FuncionarioView, Void, FuncionarioViewResponse> {
    FuncionarioViewMapper INSTANCE = Mappers.getMapper(FuncionarioViewMapper.class);

    @Mapping(source = "funcionarioId", target = "codFuncionario")
    @Mapping(source = "pessoaId", target = "codPessoa")
    @Mapping(source = "agenciaId", target = "codAgencia")
    @Mapping(source = "cargoId", target = "codCargo")
    FuncionarioViewResponse toResponse(FuncionarioView entity);
    
}