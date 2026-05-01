package br.com.store.hair.mapper;

import br.com.store.hair.dto.BrandCreateDTO;
import br.com.store.hair.dto.BrandDTO;
import br.com.store.hair.entities.BrandEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface BrandMapper {

    BrandDTO toDTO(BrandEntity entity);

    BrandEntity toEntity(BrandDTO dto);

    List<BrandDTO>toListDTO(List<BrandEntity> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    BrandEntity toNewBrandEntity(BrandCreateDTO dto);
}
