package com.arkksoft.store.services;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.arkksoft.store.dto.ProductDTO;
import com.arkksoft.store.models.dao.CategoryDao;
import com.arkksoft.store.models.dao.ProductDao;
import com.arkksoft.store.models.entity.Product;

@Service
public class ProductService {
    @Autowired
    private ProductDao productDao;

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private CloudinaryService cloudinaryService;

    public Map<String, Object> addProduct(ProductDTO productDTO, MultipartFile file) {
        Map<String, Object> data = new HashMap<>();
        Long categoryId = productDTO.getCategory();

        String msg = "La categoria  con id: "+categoryId+" no existe en la base de datos!!";

        var category = categoryDao.findById(categoryId);

        if(category.isEmpty()) {
            throw new RuntimeException(msg);
        }

        String imageUrl = cloudinaryService.upload(file);
        productDTO.setImage(imageUrl);
        
        Product product = mapFromDto(productDTO);
        product.setCategoryId(categoryId);

        data.put("product", productDao.save(product));
       
        return data;
    }

    public Map<String, Object> addProductImage(Long id, MultipartFile file) {
        Map<String, Object> data = new HashMap<>();
        Optional<Product> product = productDao.findById(id);

        if(product.isEmpty()) 
            throw new RuntimeException("El PRODUCTO NO EXISTE");

        String imageUrl = cloudinaryService.upload(file);
        product.get().setImage(imageUrl);

        data.put("product", productDao.save(product.get()));

        return data;
    }

    public Map<String, Object> getAllProducts() {
        Map<String, Object> data = new HashMap<>();

        data.put("products", productDao.getAllProducts());
        return data;
    }

    public Map<String, Object> getProduct(Long id) {
        Map<String, Object> data = new HashMap<>();
        String msg = "El producto  con id: "+id+" no existe en la base de datos!!";

        var product = productDao.findById(id)
                                    .orElseThrow(() -> new RuntimeException(msg));

        data.put("product", product);
        return data;
    }

    public Map<String, Object> deleteProduct(Long id) {
        Map<String, Object> data = new HashMap<>();
        String msg = "El producto con id: "+id+" no existe en la base de datos!!";

        var product = productDao.findById(id)
        .orElseThrow(() -> new RuntimeException(msg));

        String imageId = getImageId(product.getImage());
        cloudinaryService.deleteFile(imageId);

        productDao.delete(product);

        data.put("message", "Producto con id: "+id+" fue eliminado exitosamente!!");
        return data;
    }

    public Map<String, Object> updateProduct(ProductDTO productDTO, MultipartFile file) {
        Map<String, Object> data = new HashMap<>();

        this.updateImage(productDTO.getId());    
        String imageUrl = cloudinaryService.upload(file);
        productDTO.setImage(imageUrl); 
    
        Product product = mapFromDto(productDTO);

        data.put("product", productDao.save(product));

        return data;
    }

    public Map<String, Object> updateProductNotImage(ProductDTO productDTO) {
        Map<String, Object> data = new HashMap<>();
       
        Product product = mapFromDto(productDTO);
        data.put("product", productDao.save(product));

        return data;
    }

    /*
     * Fuction Helpers
     * 
     */

    private void updateImage(Long productId) {
        Product product = productDao.findById(productId).get();
        String imageId = getImageId(product.getImage());
        cloudinaryService.deleteFile(imageId);
    }

    private String getImageId(String imageUrl) {
        String[] imgArray = imageUrl.split("/");
        String imgId = imgArray[imgArray.length-1].split("\\.")[0];

        return imgId;
    }
    
    private Product mapFromDto(ProductDTO productDTO) {
        ModelMapper modelMapper = new ModelMapper();
        Product product = modelMapper.map(productDTO, Product.class);

        return product;
    }

    /* private ProductDTO mapToDto(Product product) {
        ModelMapper modelMapper = new ModelMapper();
        ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);

        return productDTO;
    } */
}
