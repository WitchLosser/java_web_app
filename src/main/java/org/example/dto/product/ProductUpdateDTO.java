package org.example.dto.product;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class ProductUpdateDTO {
    private String name;
    private String description;
    private Double price;
    private int categoryId;
    private List<MultipartFile> images; // List of MultipartFile for multiple images
    // Other fields, getters, setters
}