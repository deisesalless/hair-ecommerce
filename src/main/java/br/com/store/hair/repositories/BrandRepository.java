package br.com.store.hair.repositories;

import br.com.store.hair.entities.BrandEntity;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<BrandEntity, Integer> {
    Optional<BrandEntity> findByNameIgnoreCase(@NotBlank(message = "O nome da marca é obrigatório") String name);
}
