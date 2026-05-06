package br.com.store.hair.controllers;

import br.com.store.hair.dto.BrandCreateDTO;
import br.com.store.hair.dto.BrandDTO;
import br.com.store.hair.services.BrandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/brands")
@RequiredArgsConstructor
@Validated
@Slf4j
public class BrandController {
    private final BrandService service;

    @GetMapping
    @Operation(summary = "Listar todas as marcas", description = "Retorna uma lista com todas as marcas cadastradas no sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    })
    public ResponseEntity<List<BrandDTO>> findAll() {
        log.info("Iniciando requisição para listar todas as marcas");
        List<BrandDTO> brands = service.findAll();
        log.info("Listagem de marcas concluída. Total encontrado: {}", brands.size());
        return ResponseEntity.ok(brands);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar marca por ID", description = "Retorna os detalhes de uma marca específica com base no ID fornecido.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Marca não encontrada")
    })
    public ResponseEntity<BrandDTO> findById(@PathVariable @Positive(message = "O id deve ser um valor positivo maior que zero") Integer id) {
        log.info("Buscando marca por ID: {}", id);
        BrandDTO brand = service.findById(id);
        log.info("Marca encontrada com sucesso. ID: {}", id);
        return ResponseEntity.ok(brand);
    }

    @PostMapping
    @Operation(summary = "Criar nova marca", description = "Cria uma nova marca no sistema com base nos dados fornecidos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Marca criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos na requisição")
    })
    public ResponseEntity<BrandDTO> createNewBrand(@RequestBody @Valid BrandCreateDTO dto) {
        log.info("Iniciando criação de nova marca. Nome: {}", dto.name());
        BrandDTO createdBrand = service.createNewBrand(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdBrand.id())
                .toUri();
        log.info("Marca criada com sucesso. ID: {}", createdBrand.id());
        return ResponseEntity.created(location).body(createdBrand);
    }

    @DeleteMapping("/disable/{id}")
    @Operation(summary = "Desativar marca", description = "Realiza a desativação lógica de uma marca pelo ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Marca desativada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Marca não encontrada")
    })
    public ResponseEntity<Void> disableBrand(@PathVariable @Positive(message = "O id deve ser um valor positivo maior que zero") Integer id) {
        log.warn("Desativando marca. ID: {}", id);
        service.disableBrand(id);
        log.warn("Marca desativada com sucesso. ID: {}", id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/enable/{id}")
    @Operation(summary = "Ativar marca", description = "Reativa uma marca previamente desativada.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Marca ativada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Marca não encontrada")
    })
    public ResponseEntity<Void> enableBrand(@PathVariable @Positive(message = "O id deve ser um valor positivo maior que zero") Integer id) {
        log.info("Ativando marca. ID: {}", id);
        service.enableBrand(id);
        log.info("Marca ativada com sucesso. ID: {}", id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/update-name")
    @Operation(summary = "Atualizar nome da marca", description = "Atualiza o nome de uma marca existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Nome atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Marca não encontrada")
    })
    public ResponseEntity<BrandDTO> updateBrandName(@PathVariable @Positive(message = "O id deve ser um valor positivo maior que zero") Integer id,
                                                @RequestParam @NotBlank(message = "O nome da marca não pode estar em branco") String newName) {
        log.info("Atualizando nome da marca. ID: {}, Novo nome: {}", id, newName);
        BrandDTO updated = service.updateBrandName(id, newName);
        log.info("Nome da marca atualizado com sucesso. ID: {}", id);
        return ResponseEntity.ok(updated);
    }
}
