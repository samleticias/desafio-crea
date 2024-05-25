package samleticias.desafiocreaapi.rest.dto;

import jakarta.validation.constraints.NotBlank;

public record TitleDTO(
        @NotBlank(message = "Informe a descrição do título.")
        String description
) {
}
