package org.example.mappers;


import org.example.dto.productimage.ProductImageDTO;
import org.example.entities.ProductImageEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductImageMapper {
    @Mapping(target = "productId", source = "product.id")
    ProductImageDTO productImageToItemDTO(ProductImageEntity productImage);
}
