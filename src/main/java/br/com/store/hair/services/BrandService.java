package br.com.store.hair.services;

import br.com.store.hair.dto.BrandDTO;
import br.com.store.hair.mapper.BrandMapper;
import br.com.store.hair.repositories.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BrandService {
    private final BrandRepository repository;
    private final BrandMapper mapper;

    public List<BrandDTO> findAll() {
        return mapper.toListDTO(repository.findAll());
    }

    public Optional<BrandDTO> findById(Integer id) {
        return repository.findById(id).map(mapper::toDTO);
    }
}
