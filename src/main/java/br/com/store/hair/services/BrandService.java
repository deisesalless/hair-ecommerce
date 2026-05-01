package br.com.store.hair.services;

import br.com.store.hair.dto.BrandDTO;
import br.com.store.hair.mapper.BrandMapper;
import br.com.store.hair.repositories.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BrandService {
    private final BrandRepository repository;
    private final BrandMapper mapper;

    public List<BrandDTO> findAll() {
        return mapper.toListDTO(repository.findAll());
    }
}
