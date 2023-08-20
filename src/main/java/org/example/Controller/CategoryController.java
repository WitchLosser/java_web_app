package org.example.Controller;

import org.example.DAOs.CategoryDao;
import org.example.dto.category.CategoryItemDTO;
import org.example.entities.Category;
import org.example.utils.HibernateUtil;
import org.hibernate.SessionFactory;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    SessionFactory sf = HibernateUtil.getSessionFactory();
    CategoryDao categoryDao = new CategoryDao(sf);
    ModelMapper modelMapper = new ModelMapper();


    @GetMapping("/{id}")
    public ResponseEntity<CategoryItemDTO> getCategory(@PathVariable int id) {
        Category category = categoryDao.getCategoryById(id);
        if (category != null) {
            CategoryItemDTO categoryDTO = modelMapper.map(category, CategoryItemDTO.class);
            return new ResponseEntity<>(categoryDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<CategoryItemDTO>> getAllCategories() {
        List<Category> categories = categoryDao.getAllCategories();
        List<CategoryItemDTO> categoryDTOs = categories.stream()
                .map(category -> modelMapper.map(category, CategoryItemDTO.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(categoryDTOs, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> createCategory(@RequestBody CategoryItemDTO categoryDTO) {
        Category category = modelMapper.map(categoryDTO, Category.class);
        categoryDao.createCategory(category);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCategory(@PathVariable int id, @RequestBody CategoryItemDTO updatedCategory) {
        Category existingCategory = categoryDao.getCategoryById(id);
        if (existingCategory != null) {
            modelMapper.map(updatedCategory, existingCategory);
            existingCategory.setId(id);
            categoryDao.updateCategory(existingCategory);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable int id) {
        Category category = categoryDao.getCategoryById(id);
        if (category != null) {
            categoryDao.deleteCategory(category);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
