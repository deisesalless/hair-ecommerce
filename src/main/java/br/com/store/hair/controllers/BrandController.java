package br.com.store.hair.controllers;

import br.com.store.hair.dto.BrandDTO;
import br.com.store.hair.services.BrandService;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/brands")
@RequiredArgsConstructor
public class BrandController {
    private final BrandService service;

    @GetMapping
    public ResponseEntity<List<BrandDTO>> findAll() {
        List<BrandDTO> listDTO = service.findAll();
        return listDTO.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(listDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BrandDTO> findById(@PathVariable @NotNull @Positive Integer id) {
        Optional<BrandDTO> brandDTO = service.findById(id);
        return brandDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
