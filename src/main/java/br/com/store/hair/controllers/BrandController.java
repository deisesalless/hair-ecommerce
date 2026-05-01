package br.com.store.hair.controllers;

import br.com.store.hair.dto.BrandDTO;
import br.com.store.hair.services.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
}
