package br.com.store.hair.dto;

public record BrandDTO(
        Integer id,
        String name,
        Boolean isActive,
        String createdAt
) {
}
