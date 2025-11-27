package ao.prumo.obra.obramanagementservice.entity.dto.response;

import ao.prumo.obra.obramanagementservice.entity.Agencia;
import ao.prumo.obra.obramanagementservice.utils.base.BaseEntityResponse;
import jakarta.persistence.Column;
import lombok.*;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AgenciaResponse
{
    private UUID codAgencia;
    private String abreviacao;
    private String descricao;
    private Boolean status;

}
