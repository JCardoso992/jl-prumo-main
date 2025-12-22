package ao.prumo.obra.obramanagementservice.entity.dto.request;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class LogisticaRequest
{
    private UUID codLogistiga;
    private Date dataCriacao;
    private Integer quantiaTotal;
    private Integer quantiaDisponivel;
    private UUID codMercadoria;

}