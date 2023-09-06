package org.example.services;

import lombok.AllArgsConstructor;
import org.example.dto.product.ProductCreateDTO;
import org.example.dto.product.ProductItemDTO;
import org.example.dto.product.ProductUpdateDTO;
import org.example.entities.CategoryEntity;
import org.example.entities.ProductEntity;
import org.example.entities.ProductImageEntity;
import org.example.interfaces.ProductService;
import org.example.mappers.ProductMapper;
import org.example.repositories.ProductImageRepository;
import org.example.repositories.ProductRepository;
import org.example.storage.StorageService;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final StorageService storageService;
    private final ProductMapper productMapper;
    @Override
    public ProductItemDTO create(ProductCreateDTO model) {
        var p = new ProductEntity();
        var cat = new CategoryEntity();
        cat.setId(model.getCategory_id());
        p.setName(model.getName());
        p.setDescription(model.getDescription());
        p.setPrice(model.getPrice());
        p.setDateCreated(new Date());
        p.setCategory(cat);
        p.setDelete(false);
        productRepository.save(p);
        int priority=1;
        for (var img : model.getFiles()) {
            var file = storageService.saveMultipartFile(img);
            ProductImageEntity pi = new ProductImageEntity();
            pi.setName(file);
            pi.setDateCreated(new Date());
            pi.setPriority(priority);
            pi.setDelete(false);
            pi.setProduct(p);
            productImageRepository.save(pi);
            priority++;
        }
        return null;
    }

    @Override
    public ProductItemDTO edit(int id, ProductUpdateDTO model) {
        var p = productRepository.findById(id);
        if(p.isPresent())
        {
            var product = p.get();
            for (var name: model.getRemoveFiles()) {
                var pi = productImageRepository.findByName(name);
                if(pi!=null)
                {
                    productImageRepository.delete(pi);
                    storageService.removeFile(name);
                }
            }
            var cat = new CategoryEntity();
            cat.setId(model.getCategory_id());
            product.setName(model.getName());
            product.setDescription(model.getDescription());
            product.setPrice(model.getPrice());
            product.setDateCreated(new Date());
            product.setCategory(cat);
            productRepository.save(product);
            var productImages = product.getProductImages();
            int priority=1;
            for (var pi : productImages)
            {
                if(pi.getPriority()>priority)
                    priority=pi.getPriority();
            }
            priority++;
            for (var img : model.getFiles()) {
                var file = storageService.saveMultipartFile(img);
                ProductImageEntity pi = new ProductImageEntity();
                pi.setName(file);
                pi.setDateCreated(new Date());
                pi.setPriority(priority);
                pi.setDelete(false);
                pi.setProduct(product);
                productImageRepository.save(pi);
                priority++;
            }
        }

        return null;
    }

    @Override
    public List<ProductItemDTO> get() {
        var list = new ArrayList<ProductItemDTO>();
        var data = productRepository.findAll();
        for(var product : data) {
            ProductItemDTO productItemDTO = new ProductItemDTO();

            productItemDTO.setCategory( product.getCategory().getName() );
            productItemDTO.setId( product.getId() );
            productItemDTO.setName( product.getName() );
            productItemDTO.setPrice( product.getPrice() );
            productItemDTO.setDescription( product.getDescription() );

            var items = new ArrayList<String>();
            for (var img : product.getProductImages())
            {
                items.add(img.getName());
            }
            productItemDTO.setFiles(items);
            list.add(productItemDTO);
        }
        return list;
    }

    @Override
    public ProductItemDTO getById(int id) {
        var productOptinal = productRepository.findById(id);
        if(productOptinal.isPresent())
        {
            var product = productOptinal.get();
            var data =  productMapper.ProductItemDTOByProduct(product);
            for(var img : product.getProductImages())
                data.getFiles().add(img.getName());
            return data;
        }
        return null;
    }
    @Override
    public void delete(int id) {
        ProductEntity product = productRepository.findById(id).get();
        for(var img : product.getProductImages())
            storageService.removeFile(img.getName());;
        productRepository.deleteById(id);
    }

}
