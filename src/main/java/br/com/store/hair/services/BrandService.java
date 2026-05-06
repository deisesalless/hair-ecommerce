package br.com.store.hair.services;

import br.com.store.hair.dto.BrandCreateDTO;
import br.com.store.hair.dto.BrandDTO;
import br.com.store.hair.exceptions.DuplicateBrandNameException;
import br.com.store.hair.exceptions.ResourceNotFoundException;
import br.com.store.hair.mapper.BrandMapper;
import br.com.store.hair.repositories.BrandRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BrandService {
    private static final String ENTITY_NAME = "Marca";
    private final BrandRepository repository;
    private final BrandMapper mapper;

    @Transactional(readOnly = true)
    public List<BrandDTO> findAll() {
        log.info("Iniciando busca por todas as marcas");
        var list = repository.findAll();
        if (list.isEmpty()) {
            log.warn("Nenhuma marca encontrada");
            throw new ResourceNotFoundException(ENTITY_NAME);
        }
        log.info("Busca concluída. Total de marcas encontradas: {}", list.size());
        return mapper.toListDTO(list);
    }

    @Transactional(readOnly = true)
    public BrandDTO findById(Integer id) {
        log.info("Buscando marca por ID: {}", id);
        var entity = repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Marca não encontrada. ID: {}", id);
                    return new ResourceNotFoundException(ENTITY_NAME, id);
                });
        log.info("Marca encontrada com sucesso. ID: {}", id);
        return mapper.toDTO(entity);
    }

    @Transactional
    public BrandDTO createNewBrand(BrandCreateDTO brandDTO) {
        log.info("Iniciando criação de nova marca. Nome: {}", brandDTO.name());
        repository.findByNameIgnoreCase(brandDTO.name()).ifPresent(b -> {
            log.warn("Tentativa de criação de marca duplicada. Nome: {}", brandDTO.name());
            throw new DuplicateBrandNameException(brandDTO.name());
        });
        var brandEntity = mapper.toNewBrandEntity(brandDTO);
        var savedEntity = repository.save(brandEntity);
        log.info("Marca criada com sucesso. ID: {}, Nome: {}", savedEntity.getId(), savedEntity.getName());
        return mapper.toDTO(savedEntity);
    }

    @Transactional
    public void disableBrand(Integer id) {
        log.warn("Iniciando desativação da marca. ID: {}", id);
        var brandEntity = repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Marca não encontrada para desativação. ID: {}", id);
                    return new ResourceNotFoundException(ENTITY_NAME, id);
                });
        brandEntity.setIsActive(false);
        repository.save(brandEntity);
        log.warn("Marca desativada com sucesso. ID: {}", id);
    }

    @Transactional
    public void enableBrand(Integer id) {
        log.info("Iniciando ativação da marca. ID: {}", id);
        var brandEntity = repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Marca não encontrada para ativação. ID: {}", id);
                    return new ResourceNotFoundException(ENTITY_NAME, id);
                });
        brandEntity.setIsActive(true);
        repository.save(brandEntity);
        log.info("Marca ativada com sucesso. ID: {}", id);
    }

    @Transactional
    public BrandDTO updateBrandName(Integer id, String newName) {
        log.info("Atualizando nome da marca. ID: {}, Novo nome: {}", id, newName);
        var brandEntity = repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Marca não encontrada para atualização. ID: {}", id);
                    return new ResourceNotFoundException(ENTITY_NAME, id);
                });
        String oldName = brandEntity.getName();
        brandEntity.setName(newName);
        var updatedBrand = repository.save(brandEntity);
        log.info("Nome da marca atualizado com sucesso. ID: {}, Nome antigo: {}, Novo nome: {}",
                id, oldName, newName);
        return mapper.toDTO(updatedBrand);
    }
}
