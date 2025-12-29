package ao.prumo.obra.obramanagementservice.entity.dto.request;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrganizacaoRequest
{
    @NotNull(message = "[telefone] é um campo é obrigatório.")
    @NotBlank(message = "[telefone], este campo não pode ficar em branco.")
    private String telefone1;
    private String telefone2;
    @NotNull(message = "[email] é um campo é obrigatório.")
    @NotBlank(message = "[email], este campo não pode ficar em branco.")
    private String email1;
    private String email2;
    private String objecto;
    @NotNull(message = "[nif] é um campo é obrigatório.")
    @NotBlank(message = "[nif], este campo não pode ficar em branco.")
    private String nif;
    private String numMatricula;
    private String firma;
    private String razaoSocial;
    //@NotNull(message = "[logotipo] é um campo é obrigatório.")
    //@NotBlank(message = "[logotipo], este campo não pode ficar em branco.")
    //private String arquivoPath;
    private String webPage;
    private String regime;
    //KeyCloak
    private UUID keycloak_realm_id;

    // Objeto aninhado para criar uma nova Organizacao junto com o Endereco
    @Valid // Adiciona validação em cascata para o objeto OrganizacaoRequest
    @NotNull(message = "Os dados do endereço são obrigatórios.")
    private EnderecoRequest codAdress;
}
