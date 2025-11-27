package ao.prumo.obra.obramanagementservice.entity.dto.response;

import ao.prumo.obra.obramanagementservice.entity.*;
import ao.prumo.obra.obramanagementservice.utils.base.BaseEntityResponse;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Component
public class ProjetoArquitetonicoResponse extends BaseEntityResponse<ProjetoArquitetonico, ProjetoArquitetonicoResponse>
{
    private UUID codProjetoArquitetonico;
    private String codigoProcesso;
    private String notaInformativa;
    private String descricao;
    private Date dataInicio;
    private Date dataTermino;
    private Double orcamentoTotal;
    private Double saldoCaixa;
    private ClienteResponse codCliente;
    private OrganizacaoResponse codOrganizacao;
    private EnderecoResponse codEndereco;
    private FuncionarioResponse codArquitectoChefe;
    private Boolean status;

    @Override
    public ProjetoArquitetonicoResponse convertToDTO(ProjetoArquitetonico entity) {
        ProjetoArquitetonicoResponse resp = new ProjetoArquitetonicoResponse();
        BeanUtils.copyProperties(entity, resp);
        resp.setCodProjetoArquitetonico(entity.getId());
        if (entity.getClienteId() != null){
        resp.setCodCliente(new ClienteResponse().convertToDTO(entity.getClienteId()));}
       // if (entity.getEnderecoId()!=null){
       // resp.setCodEndereco(new EnderecoResponse().convertToDTO(entity.getEnderecoId()));}
        if (entity.getArquitectoChefeId()!=null){
        resp.setCodArquitectoChefe(new FuncionarioResponse().convertToDTO(entity.getArquitectoChefeId()));}
        if (entity.getOrganizacaoId()!=null){
        resp.setCodOrganizacao(new OrganizacaoResponse().convertToDTO(entity.getOrganizacaoId()));}
        return resp;
    }

    @Override
    public List<ProjetoArquitetonicoResponse> listToDTO(List<ProjetoArquitetonico> list) {
        return list.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public HashMap paginar(Page page) {
        return super.paginar(page);
    }
}
