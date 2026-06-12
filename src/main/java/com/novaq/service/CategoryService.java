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

    public CategoryResponseDTO createCategory (CategoryRequestDTO request){

        if (categoryRepository.findByNome(request.nome()).isPresent()){
            throw new IllegalArgumentException("A categoria já existe");
        }

        Category category1 = new Category();
        category1.setNome(request.nome());

        Category categorySaved = categoryRepository.save(category1);

        return new CategoryResponseDTO(
                categorySaved.getId(),
                categorySaved.getNome()
        );
    }


}
