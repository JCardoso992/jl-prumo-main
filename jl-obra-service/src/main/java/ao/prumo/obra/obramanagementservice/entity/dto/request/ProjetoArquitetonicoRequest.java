package ao.prumo.obra.obramanagementservice.entity.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjetoArquitetonicoRequest
{

    private String notaInformativa;
    private String descricao;
    private Date dataInicio;
    private Date dataTermino;
    private Double orcamentoTotal;
    private Double saldoCaixa;
    private UUID codcliente;
    private UUID codArquitectoChefe;

    // Objeto aninhado para criar uma novo Endereço junto com o Projecto
    @Valid // Adiciona validação em cascata para o objeto EndereçoRequest
    @NotNull(message = "Os dados da pessoa são obrigatórios.")
    private EnderecoRequest codEndereco;
}
