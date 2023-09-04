package org.example.mappers;

import org.example.dto.product.ProductCreateDTO;
import org.example.dto.product.ProductItemDTO;
import org.example.dto.product.ProductUpdateDTO;
import org.example.entities.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    @Mapping(target = "categoryName", source = "category.name")
    ProductItemDTO productToItemDTO(ProductEntity productEntity);

    List<ProductItemDTO> productsToItemDTOs(List<ProductEntity> productEntities);

    ProductEntity createDTOToEntity(ProductCreateDTO createDTO);

    @Mapping(target = "id", ignore = true) // Ignore the id when updating
    @Mapping(target = "category.id", source = "categoryId")
    ProductEntity updateDTOToEntity(ProductUpdateDTO updateDTO, Long categoryId);
}