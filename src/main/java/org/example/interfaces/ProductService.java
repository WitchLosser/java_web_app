package org.example.interfaces;

import org.example.dto.product.ProductCreateDTO;
import org.example.dto.product.ProductItemDTO;
import org.example.dto.product.ProductUpdateDTO;

import java.util.List;

public interface ProductService {
    ProductItemDTO create(ProductCreateDTO model);
    ProductItemDTO edit(int id, ProductUpdateDTO model);
    List<ProductItemDTO> get();
    ProductItemDTO getById(int id);
    void delete(int id);
}