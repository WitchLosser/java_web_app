package org.example.dto.productimage;

import lombok.Data;

@Data
public class ProductImageDTO {
    private int id;
    private int productId;
    private String image;
}
