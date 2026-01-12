package ao.prumo.obra.obramanagementservice.entity;

import ao.prumo.obra.obramanagementservice.entity.enums.EstadoCivil;
import ao.prumo.obra.obramanagementservice.entity.enums.Identificacao;
import ao.prumo.obra.obramanagementservice.entity.enums.Sexo;
import ao.prumo.obra.obramanagementservice.entity.enums.EstadoCliente;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import java.util.Map;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "vw_cliente_detalhado")
@Immutable
@Getter
@Setter
public class ClienteView 
{

    @Id
    @Column(name = "cliente_id")
    private UUID clienteId;

    private LocalDateTime createdDate;
    private String telefone1;
    private String telefone2;
    private String email;
    private String fax;
    private String arquivoPath;
    private String enderecoWeb;
    private Boolean status;

    @Enumerated(EnumType.STRING)
    private EstadoCliente estadoCliente;

    @Column(name = "organizacao_id")
    private UUID organizacaoId;

    // Pessoa
    private UUID pessoaId;
    private String nome;
    private String nomePai;
    private String nomeMae;
    private String nif;
    private Double altura;
    private Date dateOfBird;
    private Date emitidoEm;
    private Date validoAte;

    @Enumerated(EnumType.STRING)
    private Sexo sexo;
    @Enumerated(EnumType.STRING)
    private EstadoCivil estadoCivil;
    @Enumerated(EnumType.STRING)
    private Identificacao documentoIdentificacao;

    // Endereço
    private String endereco;
    private String cidade;

}
