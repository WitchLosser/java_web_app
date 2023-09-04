package org.example.services;

import jakarta.persistence.EntityNotFoundException;
import org.example.dto.product.ProductCreateDTO;
import org.example.dto.product.ProductItemDTO;
import org.example.dto.product.ProductUpdateDTO;
import org.example.entities.CategoryEntity;
import org.example.entities.ProductEntity;
import org.example.mappers.ProductMapper;
import org.example.repositories.CategoryRepository;
import org.example.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductMapper productMapper;

    public ProductItemDTO createProduct(int categoryId, ProductCreateDTO productCreateDTO) {
        // Validate and fetch the category
        CategoryEntity category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with ID: " + categoryId));

        // Create a new product entity
        ProductEntity productEntity = productMapper.createDTOToEntity(productCreateDTO);
        productEntity.setCategory(category);

        // Save the product entity
        ProductEntity savedProduct = productRepository.save(productEntity);

        return productMapper.productToItemDTO(savedProduct);
    }

    public ProductItemDTO getProductItemDTOById(int productId) {
        ProductEntity productEntity = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + productId));
        return productMapper.productToItemDTO(productEntity);
    }

    public ProductItemDTO updateProduct(int productId, ProductUpdateDTO productUpdateDTO) {
        // Fetch the existing product
        ProductEntity existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + productId));

        // Update the product entity with the provided data
        existingProduct.setName(productUpdateDTO.getName());
        existingProduct.setDescription(productUpdateDTO.getDescription());

        // Fetch the new category or use the existing one
        CategoryEntity category = categoryRepository.findById(productUpdateDTO.getCategoryId())
                .orElse(existingProduct.getCategory());

        existingProduct.setCategory(category);

        // Save the updated product
        ProductEntity updatedProduct = productRepository.save(existingProduct);

        return productMapper.productToItemDTO(updatedProduct);
    }

    public void deleteProduct(int productId) {
        // Check if the product exists
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + productId));

        // Delete the product
        productRepository.delete(product);
    }

    // Other methods as needed...
}

