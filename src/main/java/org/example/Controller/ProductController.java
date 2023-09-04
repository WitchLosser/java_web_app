package org.example.Controller;

import org.example.dto.product.ProductCreateDTO;
import org.example.dto.product.ProductItemDTO;
import org.example.dto.product.ProductUpdateDTO;
import org.example.dto.productimage.ProductImageDTO;
import org.example.services.ProductImageService;
import org.example.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private ProductImageService productImageService;

    @GetMapping("/{productId}")
    public ResponseEntity<ProductItemDTO> getProductById(@PathVariable int productId) {
        ProductItemDTO productItemDTO = productService.getProductItemDTOById(productId);
        List<ProductImageDTO> productImages = productImageService.getProductImagesByProductId(productId);
        productItemDTO.setImages(productImages);
        return new ResponseEntity<>(productItemDTO, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ProductItemDTO> createProduct(
            @RequestParam("category_id") int categoryId,
            @RequestParam("images") List<MultipartFile> images,
            @ModelAttribute ProductCreateDTO productCreateDTO
    ) {
        ProductItemDTO createdProduct = productService.createProduct(categoryId, productCreateDTO);

        // Handle image uploads and associate them with the created product
        for (MultipartFile image : images) {
            productImageService.createProductImage(Math.toIntExact(createdProduct.getId()), image);
        }

        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductItemDTO> updateProduct(
            @PathVariable int productId,
            @RequestBody ProductUpdateDTO productUpdateDTO
    ) {
        ProductItemDTO updatedProduct = productService.updateProduct(productId, productUpdateDTO);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable int productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }
}