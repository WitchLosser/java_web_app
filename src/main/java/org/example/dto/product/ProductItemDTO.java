package org.example.dto.product;

import lombok.Data;
import org.example.dto.productimage.ProductImageDTO;

import java.util.List;

@Data
public class ProductItemDTO {
    private Long id;
    private String name;
    private String description;
    private int categoryId;
    private String categoryName;
    private List<ProductImageDTO> images; // List of ProductImageDTOs
    // Other fields, getters, setters
}