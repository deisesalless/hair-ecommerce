package br.com.store.hair.dto;

import jakarta.validation.constraints.NotBlank;

public record BrandCreateDTO(
        @NotBlank(message = "O nome da marca é obrigatório")
        String name
) {
}
