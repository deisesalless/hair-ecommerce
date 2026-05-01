package br.com.store.hair.dto;

import java.time.LocalDateTime;

public record BrandDTO(
        Integer id,
        String name,
        Boolean isActive,
        LocalDateTime createdAt
) {
}
