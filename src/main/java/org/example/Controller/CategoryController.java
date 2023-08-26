package org.example.Controller;

import lombok.AllArgsConstructor;
import org.example.entities.CategoryEntity;
import org.example.mappers.CategoryMapper;
import org.example.repositories.CategoryRepository;
import org.example.utils.ImageUtil;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.UUID;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import org.example.dto.category.*;

@RestController
@AllArgsConstructor
public class CategoryController {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    private static final String UPLOAD_DIR = "uploads";
    private static final String RESIZED_DIR = "resized";

    @PostMapping(value = "/category", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CategoryItemDTO createCategory(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("image") MultipartFile imageFile) throws IOException {

        String imageName = StringUtils.cleanPath(imageFile.getOriginalFilename());
        String uniqueImageName = generateUniqueFileName(imageName);
        Path uploadPath = Path.of(UPLOAD_DIR).toAbsolutePath().normalize();
        Path imagePath = uploadPath.resolve(uniqueImageName);

        Files.createDirectories(uploadPath);
        Files.copy(imageFile.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);

        // Save resized images
        Path resizedPath = Path.of(RESIZED_DIR).toAbsolutePath().normalize();
        Files.createDirectories(resizedPath);


        File resized150x150 = resizedPath.resolve( "150x150_" + uniqueImageName).toFile();
        ImageUtil.resizeImage(imagePath.toFile(), resized150x150, 150, 150);

        File resized300x300 = resizedPath.resolve( "300x300_" + uniqueImageName).toFile();
        ImageUtil.resizeImage(imagePath.toFile(), resized300x300, 300, 300);

        CategoryEntity cat = CategoryEntity.builder()
                .name(name)
                .description(description)
                .image(uniqueImageName)
                .build();
        categoryRepository.save(cat);
        return categoryMapper.categoryToItemDTO(cat);
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<CategoryItemDTO> getCategoryById(@PathVariable int id) {
        Optional<CategoryEntity> categoryOptional = categoryRepository.findById(id);
        return categoryOptional
                .map(category -> ResponseEntity.ok().body(categoryMapper.categoryToItemDTO(category)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/category")
    public List<CategoryItemDTO> getAllCategories() {
        List<CategoryEntity> categoryList = categoryRepository.findAll();
        return categoryMapper.listCategoriesToItemDTO(categoryList);
    }

    @PutMapping(value = "/category/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CategoryItemDTO> updateCategory(
            @PathVariable int id,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam(value = "image", required = false) MultipartFile newImageFile) throws IOException {
        Optional<CategoryEntity> categoryOptional = categoryRepository.findById(id);
        return categoryOptional.map(category -> {
            category.setName(name);
            category.setDescription(description);

            if (newImageFile != null && !newImageFile.isEmpty()) {
                    // Delete associated image files
                    String imagePathDelete = category.getImage();
                    if (imagePathDelete != null) {
                        Path imageFilePathDelete = Path.of(UPLOAD_DIR,imagePathDelete);
                        Path resized150x150FilePath = Path.of(RESIZED_DIR, "150x150_" + imageFilePathDelete.getFileName());
                        Path resized300x300FilePath = Path.of(RESIZED_DIR, "300x300_" + imageFilePathDelete.getFileName());

                        try {
                            Files.deleteIfExists(imageFilePathDelete);
                            Files.deleteIfExists(resized150x150FilePath);
                            Files.deleteIfExists(resized300x300FilePath);
                        } catch (IOException e) {
                            // Handle the exception if necessary
                            e.printStackTrace();
                        }
                    }
                    //add new photos
                String imageName = StringUtils.cleanPath(newImageFile.getOriginalFilename());
                String uniqueImageName = generateUniqueFileName(imageName);
                Path uploadPath = Path.of(UPLOAD_DIR).toAbsolutePath().normalize();
                Path imagePath = uploadPath.resolve(uniqueImageName);

                try {
                    Files.copy(newImageFile.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                // Save resized images with unique names
                Path resizedPath = Path.of(RESIZED_DIR).toAbsolutePath().normalize();
                try {
                    Files.createDirectories(resizedPath);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                File resized150x150 = resizedPath.resolve("150x150_" + uniqueImageName).toFile();
                try {
                    ImageUtil.resizeImage(imagePath.toFile(), resized150x150, 150, 150);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                File resized300x300 = resizedPath.resolve( "300x300_" + uniqueImageName).toFile();
                try {
                    ImageUtil.resizeImage(imagePath.toFile(), resized300x300, 300, 300);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                category.setImage(uniqueImageName);
            }

            categoryRepository.save(category);
            return ResponseEntity.ok().body(categoryMapper.categoryToItemDTO(category));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/category/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable int id) {
        Optional<CategoryEntity> categoryOptional = categoryRepository.findById(id);
        if (categoryOptional.isPresent()) {
            CategoryEntity category = categoryOptional.get();

            // Delete associated image files
            String imagePath = category.getImage();
            if (imagePath != null) {
                Path imageFilePath = Path.of(UPLOAD_DIR,imagePath);
                Path resized150x150FilePath = Path.of(RESIZED_DIR, "150x150_" + imageFilePath.getFileName());
                Path resized300x300FilePath = Path.of(RESIZED_DIR, "300x300_" + imageFilePath.getFileName());

                try {
                    Files.deleteIfExists(imageFilePath);
                    Files.deleteIfExists(resized150x150FilePath);
                    Files.deleteIfExists(resized300x300FilePath);
                } catch (IOException e) {
                    // Handle the exception if necessary
                    e.printStackTrace();
                }
            }

            categoryRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    private String generateUniqueFileName(String originalFileName) {
        String extension = StringUtils.getFilenameExtension(originalFileName);
        String uniqueFileName = UUID.randomUUID().toString() + "." + extension;
        return uniqueFileName;
    }
}
