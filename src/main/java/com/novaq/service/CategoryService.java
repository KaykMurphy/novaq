package com.novaq.service;

import com.novaq.dtos.request.CategoryRequestDTO;
import com.novaq.dtos.response.CategoryResponseDTO;
import com.novaq.model.Category;
import com.novaq.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryResponseDTO createCategory(CategoryRequestDTO request){

        if (categoryRepository.findByName(request.name()).isPresent()){
            throw new IllegalArgumentException("Category already exists");
        }

        Category category = new Category();
        category.setName(request.name());

        Category savedCategory = categoryRepository.save(category);

        return new CategoryResponseDTO(
                savedCategory.getId(),
                savedCategory.getName()
        );
    }

}
