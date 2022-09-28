package com.arkksoft.store.services;

import java.util.HashMap;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arkksoft.store.dto.CategoryDTO;
import com.arkksoft.store.models.dao.CategoryDao;
import com.arkksoft.store.models.entity.Category;

@Service
public class CategoryService {
    @Autowired
    private CategoryDao categoryDao;
    
    public Map<String, Object> addCategory(CategoryDTO categoryDTO) {
        Map<String, Object> data = new HashMap<>();

        var categoryFind = categoryDao.findByName(categoryDTO.getName());
        
        if(categoryFind.isPresent()) {
            throw new RuntimeException("La categoria "+categoryDTO.getName()+" ya existe"); 
        }

        Category category = mapFromDto(categoryDTO);
        data.put("category", mapToDto(categoryDao.save(category)));
       
        return data;
    }

    public Map<String, Object> getAllCategories() {
        Map<String, Object> data = new HashMap<>();

        data.put("categories", categoryDao.getAllCategories());
        return data;
    }

    public Map<String, Object> getCategory(Long id) {
        Map<String, Object> data = new HashMap<>();
        String msg = "La categoria con id: "+id+" no existe en la base de datos!!";

        var category = categoryDao.findById(id)
                                    .orElseThrow(() -> new RuntimeException(msg));

        data.put("category", category);
        return data;
    }

    public Map<String, Object> deleteCategory(Long id) {
        Map<String, Object> data = new HashMap<>();
        String msg = "La categoria con id: "+id+" no existe en la base de datos!!";

        var category = categoryDao.findById(id)
        .orElseThrow(() -> new RuntimeException(msg));

        categoryDao.delete(category);

        data.put("message", "Categoria con id: "+id+" fue eliminado exitosamente!!");
        return data;
    }

    public Map<String, Object> updateCategory(CategoryDTO categoryDTO) {
        Map<String, Object> data = new HashMap<>();
        Category category = mapFromDto(categoryDTO);
        data.put("category", categoryDao.save(category));
        return data;
    }


    /*
     * Fuction Helpers
     * 
     */
    
    private Category mapFromDto(CategoryDTO categoryDTO) {
        ModelMapper modelMapper = new ModelMapper();
        Category category = modelMapper.map(categoryDTO, Category.class);

        return category;
    }

    private CategoryDTO mapToDto(Category category) {
        ModelMapper modelMapper = new ModelMapper();
        CategoryDTO categoryDTO = modelMapper.map(category, CategoryDTO.class);

        return categoryDTO;
    }
}
