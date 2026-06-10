package com.novaq.service;

import com.novaq.dtos.request.ProductRequestDTO;
import com.novaq.dtos.response.ProductResponseDTO;
import com.novaq.dtos.response.ProductVariantResponseDTO;
import com.novaq.model.Category;
import com.novaq.model.Product;
import com.novaq.model.ProductVariant;
import com.novaq.repository.CategoryRepository;
import com.novaq.repository.ProductRepository;
import com.novaq.repository.ProductVariantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductVariantRepository productVariantRepository;

    public ProductResponseDTO createProduct(ProductRequestDTO request){

        Category category = categoryRepository.findById(request.categoriaId())
                .orElseThrow(() -> new IllegalArgumentException("Esta categoria é inválida"));

        Product product = new Product();
        product.setNome(request.nome());
        product.setDescricao(request.descricao());
        product.setMarca(request.marca());
        product.setCategoria(category);
        product.setVariacoes(new ArrayList<>());

        for (var variacaoDto : request.variacoes()){

            var skuExistente = productVariantRepository.findBySku(variacaoDto.sku());

            if (skuExistente.isPresent()) {
                throw new IllegalArgumentException("O SKU '" + variacaoDto.sku() + "' já está cadastrado no sistema.");
            }

            ProductVariant variant = new ProductVariant();
            variant.setSku(variacaoDto.sku());
            variant.setCor(variacaoDto.cor());
            variant.setQuantidadeEstoque(variacaoDto.quantidadeEstoque());
            variant.setPreco(variacaoDto.preco());
            variant.setProduto(product);

            product.getVariacoes().add(variant);
        }

        Product savedProduct = productRepository.save(product);

        List<ProductVariantResponseDTO> variacoesResponse = savedProduct.getVariacoes().stream()
                .map(v -> new ProductVariantResponseDTO(
                        v.getId(),
                        v.getSku(),
                        v.getCor(),
                        v.getQuantidadeEstoque(),
                        v.getPreco()
                ))
                .toList();

        return new ProductResponseDTO(
                savedProduct.getId(),
                savedProduct.getNome(),
                savedProduct.getDescricao(),
                savedProduct.getMarca(),
                savedProduct.getCategoria().getNome(),
                variacoesResponse
        );
    }
}
