package br.com.store.hair.services;

import br.com.store.hair.dto.BrandCreateDTO;
import br.com.store.hair.dto.BrandDTO;
import br.com.store.hair.exceptions.DuplicateBrandNameException;
import br.com.store.hair.exceptions.ResourceNotFoundException;
import br.com.store.hair.mapper.BrandMapper;
import br.com.store.hair.repositories.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BrandService {
    private static final String ENTITY_NAME = "Marca";
    private final BrandRepository repository;
    private final BrandMapper mapper;

    @Transactional(readOnly = true)
    public List<BrandDTO> findAll() {
        return mapper.toListDTO(repository.findAll());
    }

    @Transactional(readOnly = true)
    public BrandDTO findById(Integer id) {
        return repository.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException(ENTITY_NAME, id));
    }

    @Transactional
    public BrandDTO createNewBrand(BrandCreateDTO brandDTO) {
        repository.findByNameIgnoreCase(brandDTO.name()).ifPresent(b -> {
            throw new DuplicateBrandNameException(brandDTO.name());
            });

        var brandEntity = mapper.toNewBrandEntity(brandDTO);
        var savedEntity = repository.save(brandEntity);
        return mapper.toDTO(savedEntity);
    }

    @Transactional
    public void disableBrand(Integer id) {
        var brandEntity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(ENTITY_NAME, id));
        brandEntity.setIsActive(false);
        repository.save(brandEntity);
    }

    @Transactional
    public void enableBrand(Integer id) {
        var brandEntity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(ENTITY_NAME, id));
        brandEntity.setIsActive(true);
        repository.save(brandEntity);
    }

    @Transactional
    public BrandDTO updateBrandName(Integer id, String newName) {
        var brandEntity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(ENTITY_NAME, id));
        brandEntity.setName(newName);
        var updatedBrand = repository.save(brandEntity);
        return mapper.toDTO(updatedBrand);
    }
}
