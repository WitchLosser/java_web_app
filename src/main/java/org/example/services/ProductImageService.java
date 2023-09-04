package org.example.services;

import jakarta.persistence.EntityNotFoundException;
import org.example.dto.productimage.ProductImageDTO;
import org.example.entities.ProductEntity;
import org.example.entities.ProductImageEntity;
import org.example.mappers.ProductImageMapper;
import org.example.repositories.ProductImageRepository;
import org.example.repositories.ProductRepository;
import org.example.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;

@Service
public class ProductImageService {
    @Autowired
    private ProductImageRepository productImageRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductImageMapper productImageMapper;

    @Autowired
    private StorageService storageService;

    public ProductImageDTO createProductImage(int productId, MultipartFile image) {
        // Check if the product exists
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + productId));

        // Save the image using the storage service
        String fileName = storageService.saveMultipartFile(image);

        // Create a new ProductImageEntity and associate it with the product
        ProductImageEntity productImageEntity = new ProductImageEntity();
        productImageEntity.setProduct(product);
        productImageEntity.setImageName(fileName);

        // Save the product image entity
        ProductImageEntity savedProductImage = productImageRepository.save(productImageEntity);

        return productImageMapper.productImageToDTO(savedProductImage);
    }

    public List<ProductImageDTO> getProductImagesByProductId(int productId) {
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + productId));

        List<ProductImageEntity> productImages = productImageRepository.findAllById(Collections.singleton(product.getId()));
        return productImageMapper.productImagesToDTOs(productImages);
    }
    public ProductImageDTO getProductImageById(int imageId) {
        ProductImageEntity productImage = productImageRepository.findById(imageId)
                .orElseThrow(() -> new EntityNotFoundException("Product image not found with ID: " + imageId));

        return productImageMapper.productImageToDTO(productImage);
    }
    public ProductImageDTO updateProductImage(int imageId, MultipartFile updatedImage) {
        // Check if the image exists
        ProductImageEntity existingImage = productImageRepository.findById(imageId)
                .orElseThrow(() -> new EntityNotFoundException("Product image not found with ID: " + imageId));

        // Save the updated image using the storage service
        String fileName = storageService.saveMultipartFile(updatedImage);

        // Update the image's file name
        existingImage.setImageName(fileName);

        // Save the updated product image entity
        ProductImageEntity updatedProductImage = productImageRepository.save(existingImage);

        return productImageMapper.productImageToDTO(updatedProductImage);
    }

    public void deleteProductImage(int imageId) {
        // Check if the image exists
        ProductImageEntity productImage = productImageRepository.findById(imageId)
                .orElseThrow(() -> new EntityNotFoundException("Product image not found with ID: " + imageId));

        // Delete the image using the storage service
        storageService.removeFile(productImage.getImageName());

        // Delete the product image entity
        productImageRepository.delete(productImage);
    }

    // Other methods as needed...
}
