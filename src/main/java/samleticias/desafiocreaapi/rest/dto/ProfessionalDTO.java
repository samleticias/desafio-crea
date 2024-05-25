package samleticias.desafiocreaapi.rest.dto;

import jakarta.validation.constraints.NotBlank;
import samleticias.desafiocreaapi.domain.entities.enums.ProfessionalType;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ProfessionalDTO(
        @NotBlank(message = "Informe o código do cliente.")
        Integer id,
        @NotBlank(message = "Campo nome é obrigatório.")
        String name,
        @NotBlank(message = "Campo email é obrigatório.")
        String email,
        @NotBlank(message = "Campo senha é obrigatório.")
        String password,
        @NotBlank(message = "Campo data de nascimento é obrigatório.")
        LocalDate birthDate,
        @NotBlank(message = "Campo data de registro é obrigatório.")
        LocalDateTime registrationDate,
        @NotBlank(message = "Campo telefone é obrigatório.")
        String phone,
        @NotBlank(message = "Campo tipo de profissional é obrigatório.")
        ProfessionalType professionalType
){
}
