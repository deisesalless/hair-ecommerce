package br.com.store.hair.mapper;

import br.com.store.hair.dto.BrandDTO;
import br.com.store.hair.entities.BrandEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface BrandMapper {

    BrandDTO toDTO(BrandEntity entity);

    BrandEntity toEntity(BrandDTO dto);

    List<BrandDTO>toListDTO(List<BrandEntity> entities);
}
