package org.example.Controller;

import org.example.dto.productimage.ProductImageDTO;

import org.example.services.ProductImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/product-images")
public class ProductImageController {
    @Autowired
    private ProductImageService productImageService;

    @PostMapping("/{productId}")
    public ResponseEntity<ProductImageDTO> uploadImage(
            @PathVariable int productId,
            @RequestParam("file") MultipartFile file) {
        ProductImageDTO createdImage = productImageService.createProductImage(productId, file);
        return new ResponseEntity<>(createdImage, HttpStatus.CREATED);
    }

    @GetMapping("/{imageId}")
    public ResponseEntity<ProductImageDTO> getImageById(@PathVariable int imageId) {
        ProductImageDTO productImage = productImageService.getProductImageById(imageId);
        return new ResponseEntity<>(productImage, HttpStatus.OK);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ProductImageDTO>> getImagesByProductId(@PathVariable int productId) {
        List<ProductImageDTO> productImages = productImageService.getProductImagesByProductId(productId);
        return new ResponseEntity<>(productImages, HttpStatus.OK);
    }

    @PutMapping("/{imageId}")
    public ResponseEntity<ProductImageDTO> updateImage(
            @PathVariable int imageId,
            @RequestParam("file") MultipartFile file) {
        ProductImageDTO updatedImage = productImageService.updateProductImage(imageId, file);
        return new ResponseEntity<>(updatedImage, HttpStatus.OK);
    }

    @DeleteMapping("/{imageId}")
    public ResponseEntity<Void> deleteImage(@PathVariable int imageId) {
        productImageService.deleteProductImage(imageId);
        return ResponseEntity.noContent().build();
    }

    // Other endpoints as needed...
}
