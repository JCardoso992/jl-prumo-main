package ao.prumo.obra.obramanagementservice.entity.dto.mapper;

import ao.prumo.obra.obramanagementservice.entity.PagamentoProjecto;
import ao.prumo.obra.obramanagementservice.entity.ProjetoArquitetonico;
import ao.prumo.obra.obramanagementservice.entity.ContaOrganizacao;
import ao.prumo.obra.obramanagementservice.entity.dto.request.PagamentoProjectoRequest;
import ao.prumo.obra.obramanagementservice.entity.dto.response.PagamentoProjectoResponse;
import ao.prumo.obra.obramanagementservice.entity.dto.response.ProjetoArquitetonicoResponse;
import ao.prumo.obra.obramanagementservice.entity.dto.response.ContaOrganizacaoResponse;
import ao.prumo.obra.obramanagementservice.utils.base.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ProjetoArquitetonicoMapper.class, ContaOrganizacaoMapper.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface PagamentoProjectoMapper extends BaseMapper<PagamentoProjecto, PagamentoProjectoRequest, PagamentoProjectoResponse>
{
    PagamentoProjectoMapper INSTANCE = Mappers.getMapper(PagamentoProjectoMapper.class);

    // Mapeamento de PagamentoProjecto para PagamentoProjectoResponse
    @Mapping(source = "id", target = "codPagamento")
    @Mapping(source = "projetoArquitetonicoId", target = "codProjetoArquitetonico", qualifiedByName = "mapProjetoArquitetonicoToResponse")
    @Mapping(source = "contaOrganizacaoId", target = "codContaOrganizacao", qualifiedByName = "mapContaOrganizacaoToResponse")
    PagamentoProjectoResponse toResponse(PagamentoProjecto entity);

    // Mapeamento de PagamentoProjectoRequest para PagamentoProjecto
    //@Mapping(source = "codPessoaId", target = "pessoaId", qualifiedByName = "mapPessoaRequestToEntity")
    // Ignorar as propriedades que são gerenciadas pelo JPA/BD
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedData", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    PagamentoProjecto toEntity(PagamentoProjectoRequest req);

    List<PagamentoProjectoResponse> listToResponse(List<PagamentoProjecto> pagamentoDeProjectos);

    // Método para mapear Pessoa para PessoaResponse (usado nos mapeamentos acima)
    @Named("mapProjetoArquitetonicoToResponse")
    default ProjetoArquitetonicoResponse mapProjetoArquitetonicoToResponse(ProjetoArquitetonico entity) {
        if (entity == null) {
            return null;
        }
        return ProjetoArquitetonicoMapper.INSTANCE.toResponse(entity);
    }

    @Named("mapContaOrganizacaoToResponse")
    default ContaOrganizacaoResponse mapContaOrganizacaoToResponse(ContaOrganizacao entity) {
        if (entity == null) {
            return null;
        }
        return ContaOrganizacaoMapper.INSTANCE.toResponse(entity);
    }
}