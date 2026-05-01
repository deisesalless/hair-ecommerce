package br.com.store.hair.controllers;

import br.com.store.hair.dto.BrandCreateDTO;
import br.com.store.hair.dto.BrandDTO;
import br.com.store.hair.services.BrandService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/brands")
@RequiredArgsConstructor
@Validated
public class BrandController {
    private final BrandService service;

    @GetMapping
    public ResponseEntity<List<BrandDTO>> findAll() {
        List<BrandDTO> listDTO = service.findAll();
        return listDTO.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(listDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BrandDTO> findById(@PathVariable @Positive(message = "O id deve ser um valor positivo maior que zero") Integer id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<BrandDTO> createNewBrand(@RequestBody @Valid BrandCreateDTO dto) {
        BrandDTO createdBrand = service.createNewBrand(dto);
        return ResponseEntity.ok(createdBrand);
    }

    @DeleteMapping("/disable/{id}")
    public ResponseEntity<Void> disableBrand(@PathVariable @Positive(message = "O id deve ser um valor positivo maior que zero") Integer id) {
        service.disableBrand(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/enable/{id}")
    public ResponseEntity<Void> enableBrand(@PathVariable @Positive(message = "O id deve ser um valor positivo maior que zero") Integer id) {
        service.enableBrand(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/update-name")
    public ResponseEntity<BrandDTO> updateBrandName(@PathVariable @Positive(message = "O id deve ser um valor positivo maior que zero") Integer id,
                                                @RequestParam String newName) {
        return ResponseEntity.ok(service.updateBrandName(id, newName));
    }
}
