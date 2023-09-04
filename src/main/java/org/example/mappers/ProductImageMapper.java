package org.example.mappers;

import org.example.dto.productimage.ProductImageDTO;
import org.example.entities.ProductImageEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductImageMapper {
    ProductImageMapper INSTANCE = Mappers.getMapper(ProductImageMapper.class);

    @Mappings({
            @Mapping(target = "productId", source = "product.id"),
            @Mapping(target = "fileName", source = "imageName") // Field name should match "imageName"
    })
    ProductImageDTO productImageToDTO(ProductImageEntity productImageEntity);

    List<ProductImageDTO> productImagesToDTOs(List<ProductImageEntity> productImageEntities);

    @Mappings({
            @Mapping(target = "id", ignore = true), // Ignore the id when creating
            @Mapping(target = "product.id", source = "productId"),
            @Mapping(target = "imageName", source = "fileName") // Field name should match "imageName"
    })
    ProductImageEntity dtoToProductImage(ProductImageDTO productImageDTO);
}