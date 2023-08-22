package org.example.Controller;


import lombok.AllArgsConstructor;
import org.example.dto.category.CategoryCreateDTO;
import org.example.dto.category.CategoryUpdateDTO;
import org.example.entities.CategoryEntity;
import org.example.repositories.CategoryRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
public class CategoryController {

    private final CategoryRepository categoryRepository;

    @PostMapping("/category")
    public CategoryEntity create(@RequestBody CategoryCreateDTO dto) {
        CategoryEntity cat = CategoryEntity
                .builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .image(dto.getImage())
                .build();
        categoryRepository.save(cat);
        return cat;
    }
    @GetMapping("/category/{id}")
    public ResponseEntity<CategoryEntity> getCategoryById(@PathVariable int id) {
        Optional<CategoryEntity> categoryOptional = categoryRepository.findById(id);
        return categoryOptional
                .map(category -> ResponseEntity.ok().body(category))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/category")
    public List<CategoryEntity> getAllCategories() {
        return categoryRepository.findAll();
    }

    @PutMapping("/category/{id}")
    public ResponseEntity<CategoryEntity> updateCategory(@PathVariable int id, @RequestBody CategoryUpdateDTO dto) {
        Optional<CategoryEntity> categoryOptional = categoryRepository.findById(id);
        return categoryOptional.map(category -> {
            category.setName(dto.getName());
            category.setDescription(dto.getDescription());
            category.setImage(dto.getImage());
            categoryRepository.save(category);
            return ResponseEntity.ok().body(category);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/category/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable int id) {
        Optional<CategoryEntity> categoryOptional = categoryRepository.findById(id);
        if (categoryOptional.isPresent()) {
            categoryRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
