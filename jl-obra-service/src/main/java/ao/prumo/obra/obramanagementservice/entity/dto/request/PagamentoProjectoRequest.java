package ao.prumo.obra.obramanagementservice.entity.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PagamentoProjectoRequest
{

    @NotNull(message = "[ValorPago] é um campo é obrigatório.")
    private Double ValorPago;
    private Double ValorRemanescente;
    private Double ValorUsado;
    @NotNull(message = "[dataPagamento] é um campo é obrigatório.")
    private Date dataPagamento;
    @NotNull(message = "[descricao] é um campo é obrigatório.")
    @NotBlank(message = "[descricao], este campo não pode ficar em branco.")
    private String descricao;
    private Boolean status;
    @NotNull(message = "[codProjetoArquitetonico] é um campo é obrigatório.")
    private UUID codProjetoArquitetonico;
    @NotNull(message = "[codContaOrganizacao] é um campo é obrigatório.")
    private UUID codContaOrganizacao;

}
