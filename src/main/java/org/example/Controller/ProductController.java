package org.example.Controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.dto.product.ProductCreateDTO;
import org.example.dto.product.ProductItemDTO;
import org.example.dto.product.ProductUpdateDTO;
import org.example.interfaces.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/products")
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductItemDTO>> index() {
        return new ResponseEntity<>(productService.get(), HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductItemDTO> create(@Valid @ModelAttribute ProductCreateDTO model) {
        var result = productService.create(model);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping(value = "{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductItemDTO> edit(@PathVariable("id") int id,
                                               @Valid @ModelAttribute ProductUpdateDTO model) {
        var result = productService.edit(id, model);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<ProductItemDTO> getProductById(@PathVariable("id") int id) {
        var product = productService.getById(id);
        if(product!=null)
        {
            return new ResponseEntity<>(product, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable("id") int productId) {
        productService.delete(productId);
        return new ResponseEntity<>("Продукта нема, зник.", HttpStatus.OK);
    }


}